/*
 * Copyright 2023 The Squall Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.sql.annotation.DocAnnotationFactory;
import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.sql.annotation.DocAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

final class DocCommentProcessor implements CommentProcessor {

    private static final String DEFAULT_ANNOTATION = "description";

    private final DocAnnotationFactory annotationFactory;
    private final MessageBundle messageBundle;
    private final List<DocAnnotation<?>> annotations = new ArrayList<>();
    private final StringBuilder textBuilder = new StringBuilder();

    private CharSequence text;
    private long textOffset;
    private ParserContext context;

    private int current;
    private int end;

    // absolute line number in the whole source
    private int lineNo;
    private int columnNo;

    private int minColumnNo;
    private int baseColumnNo;
    private int newLines;

    private int annotationLineNo;
    private int annotationColumnNo;
    private int annotationOffset;

    private int valueEndLineNo;
    private int valueEndColumnNo;
    private int valueEndOffset;

    private String annotationName;
    private boolean explicit;

    DocCommentProcessor() {
        this.annotationFactory = DocAnnotationFactory.newInstance();
        this.messageBundle = MessageBundle.forLocale(Locale.getDefault());
    }

    @Override
    public boolean canProcess(CharSequence text) {
        Objects.requireNonNull(text);

        if (text.length() < 5) {
            return false;
        }

        return (text.charAt(0) == '/'
            && text.charAt(1) == '*'
            && text.charAt(2) == '*'
            && text.charAt(text.length() - 2) == '*'
            && text.charAt(text.length() - 1) == '/');
    }

    @Override
    public void processComment(CharSequence text, Location location, ParserContext context) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(location);

        reset(text, location, context);

        while (hasChar()) {
            parseLine();
            if (hasChar() && nextChar() == '\n') {
                consumeChar();
                processNewLine();
            }
        }

        addLastAnnotation();
        this.context.addAnnotations(this.annotations);
    }

    //

    private MessageBundle messages() {
        return this.messageBundle;
    }

    private void reset(CharSequence text, Location location, ParserContext context) {
        this.text = text;
        this.textOffset = location.offset();
        this.context = context;

        this.current = 3;
        this.end = text.length() - 2;

        this.lineNo = location.lineNo();
        this.columnNo = location.columnNo() + 3;
        this.minColumnNo = location.columnNo() + 2;
        this.baseColumnNo = Integer.MAX_VALUE;

        textBuilder.setLength(0);
        this.newLines = 0;

        this.annotations.clear();
        this.annotationName = DEFAULT_ANNOTATION;
        this.explicit = false;
    }

    private void parseLine() {
        while (this.columnNo < this.minColumnNo) {
            if (checkEndOfLine()) {
                return;
            }
            consumeChar();
        }

        skipWhitespaces();
        if (checkEndOfLine()) {
            return;
        }

        int lineStart;
        var c = nextChar();
        if (c == '@') {
            handleAnnotationTag();
            lineStart = this.current;
        } else {
            lineStart = detectLineStart(this.current);
        }

        int lineEnd = this.current;

        while (!checkEndOfLine()) {
            c = nextChar();
            consumeChar();
            if (!isWhitespace(c)) {
                lineEnd = this.current;
            }
        }

        appendText(lineStart, lineEnd);
    }

    private boolean checkEndOfLine() {
        return !hasChar() || nextChar() == '\n';
    }

    private int detectLineStart(int current) {
        if (this.columnNo < this.baseColumnNo) {
            this.baseColumnNo = this.columnNo;
            return current;
        } else {
            return current - (this.columnNo - this.baseColumnNo);
        }
    }

    private void handleAnnotationTag() {
        addLastAnnotation();

        annotationName = parseAnnotationTag();
        explicit = true;

        valueEndLineNo = lineNo;
        valueEndColumnNo = columnNo;
        valueEndOffset = current;

        skipWhitespaces();
    }

    private String parseAnnotationTag() {
        annotationLineNo = this.lineNo;
        annotationColumnNo = this.columnNo;
        annotationOffset = this.current;

        consumeChar();
        while (hasChar()) {
            var c = nextChar();
            if (c == '\n' || isWhitespace(c)) {
                break;
            }
            consumeChar();
        }
        return text.subSequence(annotationOffset + 1, this.current).toString();
    }

    private boolean hasChar() {
        return this.current < this.end;
    }

    private char nextChar() {
        if (hasChar()) {
            return text.charAt(current);
        }
        throw new NoSuchElementException();
    }

    /**
     * Skips a character if exists.
     */
    private void consumeChar() {
        if (this.current < this.end) {
            this.current++;
            this.columnNo++;
        }
    }

    /**
     * Skips whitespace characters if exists.
     */
    private void skipWhitespaces() {
        while (hasChar() && isWhitespace(nextChar())) {
            consumeChar();
        }
    }

    private void processNewLine() {
        this.lineNo++;
        this.columnNo = 1;
        this.newLines++;
    }

    private void flushNewLines() {
        int count = this.newLines;
        if (!textBuilder.isEmpty()) {
            while (count-- > 0) {
                textBuilder.append('\n');
            }
        }
        this.newLines = 0;
    }

    private void appendText(int start, int end) {
        if (start < end) {
            flushNewLines();
            textBuilder.append(this.text, start, end);
            // Updates value end
            valueEndLineNo = lineNo;
            valueEndColumnNo = columnNo;
            valueEndOffset = current;
        }
    }

    private void addLastAnnotation() {
        if (annotationName != null && (explicit || !textBuilder.isEmpty())) {
            try {
                annotations.add(createAnnotation(annotationName));
            } catch (NoSuchElementException e) {
                Location loc = new Location(
                    annotationLineNo,
                    annotationColumnNo,
                    annotationOffset + textOffset);
                context.reportProblem(System.Logger.Level.ERROR,
                    messages().UNKNOWN_ANNOTATION(annotationName), loc);
            } catch (IllegalArgumentException e) {
                Location loc = new Location(
                    valueEndLineNo,
                    valueEndColumnNo,
                    valueEndOffset + textOffset);
                context.reportProblem(System.Logger.Level.ERROR,
                    messages().INVALID_ANNOTATION_VALUE(annotationName), loc);
            }
        }
        textBuilder.setLength(0);
        annotationName = null;
    }

    private DocAnnotation<?> createAnnotation(String name) {
        var annotation = annotationFactory.createAnnotation(name, textBuilder.toString());
        if (annotation == null) {
            throw new NoSuchElementException();
        }
        return annotation;
    }

    private static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t';
    }
}
