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

package dev.openclosed.squall.renderer.asciidoc;

import java.io.IOException;
import java.io.UncheckedIOException;

final class DocBuilder implements BaseDocBuilder {

    private final Appendable appendable;
    private final MarkdownConverter converter;

    private static final String[] SECTION_MARKER = {
        "=", "==", "===", "====", "=====", "======", "======="
    };

    DocBuilder(Appendable appendable) {
        this.appendable = appendable;
        this.converter = new MarkdownConverter(this);
    }

    @Override
    public DocBuilder append(CharSequence csq) {
        try {
            appendable.append(csq);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public DocBuilder append(CharSequence csq, int start, int end) {
        try {
            appendable.append(csq, start, end);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public DocBuilder append(char c) {
        try {
            appendable.append(c);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public DocBuilder appendSpace() {
        return append(' ');
    }

    @Override
    public DocBuilder appendNewLine() {
        return append('\n');
    }

    @Override
    public DocBuilder appendSectionMarker(int level) {
        if (level < 0) {
            throw new IllegalArgumentException();
        }
        if (level < SECTION_MARKER.length) {
            append(SECTION_MARKER[level]);
        }
        return this;
    }

    @Override
    public DocBuilder appendCode(CharSequence csq) {
        return append("``").append(csq).append("``");
    }

    @Override
    public DocBuilder appendHardLineBreak() {
        return append(" +").appendNewLine();
    }

    //
    DocBuilder appendMarkdownText(CharSequence csq) {
        this.converter.writeText(csq);
        return this;
    }
}
