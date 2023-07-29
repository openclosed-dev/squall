/*
 * Copyright 2022-2023 The Squall Authors
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

package dev.openclosed.squall.core.parser;

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.parser.SqlSyntaxException;
import dev.openclosed.squall.core.base.Messages;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class BaseSqlTokenizer implements SqlTokenizer {

    private final CharSequence text;

    private int lineNo;
    private int columnNo;
    private int offset;

    private int tokenLineNo;
    private int tokenColumnNo;
    private int tokenOffset;

    private int currentChar;
    private boolean finished;
    private Token token;

    private final StringBuilder stringBuilder = new StringBuilder();

    public static final int EOI = -1;
    private static final int NOT_FETCHED = -2;

    protected BaseSqlTokenizer(CharSequence text) {
        this.text = text;
        this.lineNo = 1;
        this.columnNo = 1;
        this.currentChar = NOT_FETCHED;
    }

    @Override
    public final CharSequence text() {
        return text;
    }

    @Override
    public final Token next() {
        if (this.token == null) {
            this.token = fetch();
        }
        return this.token;
    }

    @Override
    public final void consume() {
        this.token = null;
    }

    @Override
    public final int getOffset() {
        return this.offset;
    }

    @Override
    public final int getTokenOffset() {
        return this.tokenOffset;
    }

    @Override
    public final Location getLocation() {
        return new Location(lineNo, columnNo, offset);
    }

    @Override
    public final Location getTokenLocation() {
        return new Location(tokenLineNo, tokenColumnNo, tokenOffset);
    }

    //

    private Token fetch() {
        if (this.finished) {
            return Token.EOI;
        } else {
            var newToken = fetchNew();
            if (newToken == Token.EOI) {
                this.finished = true;
            }
            return newToken;
        }
    }

    protected final Token fetchNew() {
        if (!skipWhitespaces()) {
            return Token.EOI;
        }

        this.tokenOffset = this.offset;
        this.tokenLineNo = this.lineNo;
        this.tokenColumnNo = this.columnNo;

        int c = nextChar();
        if (isAlphabet(c)) {
            return processKeywordOrIdentifierToken(c);
        } else if (isDigit(c)) {
            return processNumericLiteral();
        } else if (c == '\'') {
            return processStringLiteralToken();
        } else if (c == '"') {
            return processQuotedIdentifierToken();
        } else {
            return processSymbol(c);
        }
    }

    private Token processKeywordOrIdentifierToken(int c) {
        consumeChar();
        if (c == 'B' || c == 'b') {
            if (nextChar() == '\'') {
                return processBitStringLiteralToken();
            }
        }
        while ((c = nextChar()) != EOI) {
            if (isAlphabet(c) || isDigit(c) || c == '_' || c == '$') {
                consumeChar();
            } else {
                break;
            }
        }
        var segment = extractTextSegment(tokenOffset, offset);
        var keyword = findKeyword(segment);
        if (keyword != null) {
            return keyword;
        } else {
            return new IdentifierToken(segment);
        }
    }

    protected final Token processQuotedIdentifierToken() {
        // consumes opening quote
        consumeChar();
        int c;
        while ((c = nextChar()) != '"') {
            if (c == EOI) {
                throw newUnexpectedEndException();
            }
            consumeChar();
        }
        // consumes closing quote
        consumeChar();
        return new QuotedIdentifierToken(text, tokenOffset, offset);
    }

    protected final Token processStringLiteralToken() {
        var value = processStringLiteral();
        return new StringToken(text, tokenOffset, offset, value);
    }

    private String processStringLiteral() {
        // consumes opening quote
        consumeChar();
        var builder = getEmptyStringBuilder();
        int start = tokenOffset + 1;
        int c;
        while ((c = nextChar()) != EOI) {
            consumeChar();
            if (c == '\'') {
                if (nextChar() == '\'') {
                    builder.append(text, start, offset);
                    start = offset + 1;
                    consumeChar();
                } else {
                    break;
                }
            }
        }
        if (builder.length() > 0) {
            builder.append(text, start, offset - 1);
            return builder.toString();
        } else {
            return extractTextSegment(start, offset - 1);
        }
    }

    private Token processBitStringLiteralToken() {
        // consumes '
        consumeChar();
        int c = nextChar();
        while (c != '\'') {
            if (c != '0' && c != '1') {
                throw newInvalidCharacterException(c);
            }
            consumeChar();
            c = nextChar();
        }
        consumeChar();
        return new BitStringToken(text, tokenOffset, offset);
    }

    protected final NumberToken processNumericLiteral() {
        for (;;) {
            int c = nextChar();
            if (isDigit(c)) {
                consumeChar();
            } else if (c == '.') {
                consumeChar();
                return processDecimalNumber();
            } else if (c == 'e') {
                return processExponent();
            } else {
                break;
            }
        }

        return createIntegerToken();
    }

    protected final NumberToken processDecimalNumber() {
        // current is the first char after decimal point
        for (;;) {
            int c = nextChar();
            if (isDigit(c)) {
                consumeChar();
            } else if (c == 'e') {
                return processExponent();
            } else {
                break;
            }
        }
        return createNumberToken();
    }

    private NumberToken processExponent() {
        consumeChar();
        int c = nextChar();
        if (c == '+' || c == '-') {
            consumeChar();
            c = nextChar();
        }
        if (!isDigit(c)) {
            throw newInvalidCharacterException(c);
        }
        consumeChar();
        for (;;) {
            c = nextChar();
            if (!isDigit(c)) {
                break;
            }
            consumeChar();
        }

        return createNumberToken();
    }

    protected abstract Keyword findKeyword(String name);

    protected abstract Token processSymbol(int c);

    private NumberToken createIntegerToken() {
        String s = extractNumericText().toString();
        return new NumberToken(TokenType.INTEGER, s, new BigInteger(s));
    }

    private NumberToken createNumberToken() {
        String s = extractNumericText().toString();
        return new NumberToken(TokenType.NUMBER, s, new BigDecimal(s));
    }

    private CharSequence extractNumericText() {
        String segment = extractTextSegment(tokenOffset, offset);
        if (segment.charAt(0) == '+') {
            segment = segment.substring(1);
        }
        return segment;
    }

    protected final Token processLineComment() {
        int c;
        do {
            c = nextChar();
            consumeChar();
        } while (c != '\n' && c != EOI);

        return new CommentToken(TokenType.LINE_COMMENT, text(), tokenOffset, offset);
    }

    protected final Token processBlockComment() {
        consumeChar();
        int nests = 1;
        do {
            int c = nextChar();
            if (c == '*') {
                consumeChar();
                c = nextChar();
                if (c == '/') {
                    nests--;
                }
            } else if (c == '/') {
                consumeChar();
                c = nextChar();
                if (c == '*') {
                    nests++;
                }
            }
            consumeChar();
        } while (nests > 0);

        return new CommentToken(TokenType.BLOCK_COMMENT, text(), tokenOffset, offset);
    }

    protected final int nextChar() {
        if (currentChar != NOT_FETCHED) {
            return currentChar;
        }

        if (offset < text.length()) {
            this.currentChar = text.charAt(offset);
        } else {
            this.currentChar = EOI;
        }
        return this.currentChar;
    }

    protected final void consumeChar() {
        if (currentChar < 0) {
            return;
        } else if (currentChar == '\n') {
            lineNo++;
            columnNo = 0;
        }
        offset++;
        columnNo++;
        currentChar = NOT_FETCHED;
    }

    /**
     * Skips whitespace characters.
     *
     * @return {@code true} if there exists more characters.
     */
    private boolean skipWhitespaces() {
        int c = nextChar();
        while (c != EOI) {
            if (!isWhitespace(c)) {
                return true;
            }
            consumeChar();
            c = nextChar();
        }
        return false;
    }

    private String extractTextSegment(int start, int end) {
        return text.subSequence(start, end).toString();
    }

    private StringBuilder getEmptyStringBuilder() {
        this.stringBuilder.setLength(0);
        return this.stringBuilder;
    }

    protected static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n';
    }

    protected static boolean isAlphabet(int c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
    }

    protected static boolean isDigit(int c) {
        return '0' <= c && c <= '9';
    }

    private SqlSyntaxException newSyntaxException(Message message) {
        return new SqlSyntaxException(message, getLocation());
    }

    protected final  SqlSyntaxException newInvalidCharacterException(int c) {
        if (c == EOI) {
            return newUnexpectedEndException();
        }
        return newSyntaxException(Messages.INVALID_CHARACTER((char) c));
    }

    protected final SqlSyntaxException newUnexpectedEndException() {
        return newSyntaxException(Messages.UNEXPECTED_END_OF_INPUT());
    }
}
