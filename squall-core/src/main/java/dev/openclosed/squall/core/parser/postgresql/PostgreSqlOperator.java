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

enum PostgreSqlOperator implements Operator {
    UNARY_PLUS(Type.PREFIX, 130, Associativity.RIGHT),
    UNARY_MINUS(Type.PREFIX, 130, Associativity.RIGHT),
    EXPONENTIATION(Type.BINARY, 120, Associativity.LEFT),
    MULTIPLICATION(Type.BINARY, 110, Associativity.LEFT),
    DIVISION(Type.BINARY, 110, Associativity.LEFT),
    MODULO(Type.BINARY, 110, Associativity.LEFT),
    ADDITION(Type.BINARY, 100, Associativity.LEFT),
    SUBTRACTION(Type.BINARY, 100, Associativity.LEFT),
    LESS_THAN(Type.BINARY, 80),
    GREATER_THAN(Type.BINARY, 80),
    LESS_THAN_OR_EQUAL_TO(Type.BINARY, 80),
    GREATER_THAN_OR_EQUAL_TO(Type.BINARY, 80),
    EQUAL(Type.BINARY, 80),
    NOT_EQUAL(Type.BINARY, 80),
    LOGICAL_NEGATION(Type.PREFIX, 12, Associativity.RIGHT),
    LOGICAL_CONJUNCTION(Type.BINARY, 11, Associativity.LEFT),
    LOGICAL_DISJUNCTION(Type.BINARY, 10, Associativity.LEFT);

    private final Type type;
    private final int precedence;
    private final Associativity associativity;

    PostgreSqlOperator(Type type, int precedence) {
       this(type, precedence, Associativity.NONE);
    }

    PostgreSqlOperator(Type type, int precedence, Associativity associativity) {
        this.type = type;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    @Override
    public final Type type() {
        return type;
    }

    @Override
    public final Associativity associativity() {
        return associativity;
    }

    @Override
    public final int precedence() {
        return precedence;
    }
}
