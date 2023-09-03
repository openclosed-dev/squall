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

package dev.openclosed.squall.parser.postgresql;

import dev.openclosed.squall.api.parser.MessageBundle;
import dev.openclosed.squall.parser.basic.BaseSqlTokenizer;
import dev.openclosed.squall.parser.basic.Keyword;
import dev.openclosed.squall.parser.basic.MetacommandToken;
import dev.openclosed.squall.parser.basic.SpecialSymbol;
import dev.openclosed.squall.parser.basic.Token;

import java.util.Map;

final class PostgreSqlTokenizer extends BaseSqlTokenizer {

    private final Map<String, Keyword> keywords;

    PostgreSqlTokenizer(CharSequence text) {
        this(text, MessageBundle.get(), PostgreSqlKeyword.valuesAsMap());
    }

    PostgreSqlTokenizer(
        CharSequence text,
        MessageBundle messageBundle,
        Map<String, Keyword> keywords) {
        super(text, messageBundle);
        this.keywords = keywords;
    }

    @Override
    protected Keyword findKeyword(String name) {
        return this.keywords.get(name.toUpperCase());
    }

    @Override
    protected Token processSymbol(int c) {
        return switch (c) {
            case '(' -> {
                consumeChar();
                yield SpecialSymbol.OPEN_PAREN;
            }
            case ')' -> {
                consumeChar();
                yield SpecialSymbol.CLOSE_PAREN;
            }
            case ',' -> {
                consumeChar();
                yield SpecialSymbol.COMMA;
            }
            case ';' -> {
                consumeChar();
                yield SpecialSymbol.SEMICOLON;
            }
            case '=' -> {
                consumeChar();
                yield OperatorSymbol.EQUAL;
            }
            case '%' -> {
                consumeChar();
                yield OperatorSymbol.PERCENT;
            }
            case '^' -> {
                consumeChar();
                yield OperatorSymbol.CARET;
            }
            case '*' -> {
                consumeChar();
                yield OperatorSymbol.ASTERISK;
            }
            case ':' -> {
                // Handles double colon.
                consumeChar();
                if (nextChar() == ':') {
                    consumeChar();
                    yield OperatorSymbol.DOUBLE_COLON;
                }
                yield SpecialSymbol.COLON;
            }
            case '+' -> {
                consumeChar();
                if (isDigit(nextChar())) {
                    yield processNumericLiteral();
                }
                yield OperatorSymbol.PLUS;
            }
            case '-' -> {
                consumeChar();
                c = nextChar();
                if (c == '-') {
                    yield processLineComment();
                } else if (isDigit(c)) {
                    yield processNumericLiteral();
                }
                yield OperatorSymbol.MINUS;
            }
            case '.' -> {
                consumeChar();
                if (isDigit(nextChar())) {
                    yield processDecimalNumber();
                }
                yield SpecialSymbol.PERIOD;
            }
            case '/' -> {
                consumeChar();
                if (nextChar() == '*') {
                    yield processBlockComment();
                }
                yield OperatorSymbol.SLASH;
            }
            case '<' -> {
                consumeChar();
                c = nextChar();
                if (c == '=') {
                    consumeChar();
                    yield OperatorSymbol.LESS_THAN_OR_EQUAL_TO;
                } else if (c == '>') {
                    consumeChar();
                    yield OperatorSymbol.NOT_EQUAL;
                }
                yield OperatorSymbol.LESS_THAN;
            }
            case '>' -> {
                consumeChar();
                if (nextChar() == '=') {
                    consumeChar();
                    yield OperatorSymbol.GREATER_THAN_OR_EQUAL_TO;
                }
                yield OperatorSymbol.GREATER_THAN;
            }
            case '!' -> {
                consumeChar();
                if (nextChar() == '=') {
                    consumeChar();
                    yield OperatorSymbol.NOT_EQUAL;
                }
                yield OperatorSymbol.EXCLAMATION;
            }
            case '\\' -> processMetacommand();
            default -> throw newInvalidCharacterException(c);
        };
    }

    Token processMetacommand() {
        consumeChar();
        for (int c = nextChar(); c != '\n' && c != '\\' && c != EOI;) {
            consumeChar();
            c = nextChar();
        }
        return new MetacommandToken(text(), getTokenOffset(), getOffset());
    }
}

