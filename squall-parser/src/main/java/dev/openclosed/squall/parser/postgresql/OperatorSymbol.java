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

import dev.openclosed.squall.parser.basic.OperatorGroup;
import dev.openclosed.squall.parser.basic.Token;
import dev.openclosed.squall.parser.basic.TokenType;

enum OperatorSymbol implements Token {
    ASTERISK("*", PostgreSqlOperatorGroup.MULTIPLICATION_DIVISION),
    DOUBLE_COLON("::", PostgreSqlOperatorGroup.POSTFIX_TYPECAST),
    EQUAL("=", PostgreSqlOperatorGroup.COMPARISON),
    PLUS("+", PostgreSqlOperatorGroup.ADDITION_SUBTRACTION, PostgreSqlOperatorGroup.UNARY_PLUS_MINUS),
    MINUS("-", PostgreSqlOperatorGroup.ADDITION_SUBTRACTION, PostgreSqlOperatorGroup.UNARY_PLUS_MINUS),
    SLASH("/", PostgreSqlOperatorGroup.MULTIPLICATION_DIVISION),
    PERCENT("%", PostgreSqlOperatorGroup.MULTIPLICATION_DIVISION),
    CARET("^", PostgreSqlOperatorGroup.EXPONENTIATION),
    GREATER_THAN(">", PostgreSqlOperatorGroup.COMPARISON),
    LESS_THAN("<", PostgreSqlOperatorGroup.COMPARISON),
    NOT_EQUAL("<>", PostgreSqlOperatorGroup.COMPARISON),
    LESS_THAN_OR_EQUAL_TO("<=", PostgreSqlOperatorGroup.COMPARISON),
    GREATER_THAN_OR_EQUAL_TO(">=", PostgreSqlOperatorGroup.COMPARISON),
    EXCLAMATION("!");

    private final String text;
    private final OperatorGroup binaryOperatorGroup;
    private final OperatorGroup unaryOperatorGroup;

    OperatorSymbol(String text) {
        this(text, null, null);
    }

    OperatorSymbol(String text, OperatorGroup binaryOperatorGroup) {
        this(text, binaryOperatorGroup, null);
    }

    OperatorSymbol(String text, OperatorGroup binaryOperatorGroup, OperatorGroup unaryOperatorGroup) {
        this.text = text;
        this.binaryOperatorGroup = binaryOperatorGroup;
        this.unaryOperatorGroup = unaryOperatorGroup;
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
        return this.binaryOperatorGroup != null;
    }

    @Override
    public OperatorGroup binaryOperatorGroup() {
        return this.binaryOperatorGroup;
    }

    @Override
    public boolean isUnaryOperator() {
        return this.unaryOperatorGroup != null;
    }

    @Override
    public OperatorGroup unaryOperatorGroup() {
        return this.unaryOperatorGroup;
    }
}
