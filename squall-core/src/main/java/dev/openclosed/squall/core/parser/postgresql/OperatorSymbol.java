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

package dev.openclosed.squall.core.parser.postgresql;

import dev.openclosed.squall.core.parser.Operator;
import dev.openclosed.squall.core.parser.Token;
import dev.openclosed.squall.core.parser.TokenType;

enum OperatorSymbol implements Token {
    ASTERISK("*", PostgreSqlOperator.MULTIPLICATION),
    DOUBLE_COLON("::") {
        @Override
        public boolean isPostfixOperator() {
            return true;
        }
    },
    EQUAL("=", PostgreSqlOperator.EQUAL),
    PLUS("+", PostgreSqlOperator.ADDITION, PostgreSqlOperator.UNARY_PLUS),
    MINUS("-", PostgreSqlOperator.SUBTRACTION, PostgreSqlOperator.UNARY_MINUS),
    SLASH("/", PostgreSqlOperator.DIVISION),
    PERCENT("%", PostgreSqlOperator.MODULO),
    CARET("^", PostgreSqlOperator.EXPONENTIATION),
    GREATER_THAN(">", PostgreSqlOperator.GREATER_THAN),
    LESS_THAN("<", PostgreSqlOperator.LESS_THAN),
    NOT_EQUAL("<>", PostgreSqlOperator.NOT_EQUAL),
    LESS_THAN_OR_EQUAL_TO("<=", PostgreSqlOperator.LESS_THAN_OR_EQUAL_TO),
    GREATER_THAN_OR_EQUAL_TO(">=", PostgreSqlOperator.GREATER_THAN_OR_EQUAL_TO),
    EXCLAMATION("!");

    private final String text;
    private final Operator binaryOperator;
    private final Operator prefixOperator;

    OperatorSymbol(String text) {
        this(text, null, null);
    }

    OperatorSymbol(String text, Operator binaryOperator) {
        this(text, binaryOperator, null);
    }

    OperatorSymbol(String text, Operator binaryOperator, Operator prefixOperator) {
        this.text = text;
        this.binaryOperator = binaryOperator;
        this.prefixOperator = prefixOperator;
    }

    @Override
    public TokenType type() {
        return TokenType.SYMBOL;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public boolean isBinaryOperator() {
        return this.binaryOperator != null;
    }

    @Override
    public Operator toBinaryOperator() {
        return this.binaryOperator;
    }

    @Override
    public boolean isPrefixOperator() {
        return this.prefixOperator != null;
    }

    @Override
    public Operator toPrefixOperator() {
        return this.prefixOperator;
    }
}
