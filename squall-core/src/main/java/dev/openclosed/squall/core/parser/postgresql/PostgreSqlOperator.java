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
    POSTFIX_TYPECAST(Arity.BINARY, 200, Associativity.LEFT),
    UNARY_PLUS(Arity.UNARY, 130, Associativity.RIGHT),
    UNARY_MINUS(Arity.UNARY, 130, Associativity.RIGHT),
    EXPONENTIATION(Arity.BINARY, 120, Associativity.LEFT),
    MULTIPLICATION(Arity.BINARY, 110, Associativity.LEFT),
    DIVISION(Arity.BINARY, 110, Associativity.LEFT),
    MODULO(Arity.BINARY, 110, Associativity.LEFT),
    ADDITION(Arity.BINARY, 100, Associativity.LEFT),
    SUBTRACTION(Arity.BINARY, 100, Associativity.LEFT),
    LESS_THAN(Arity.BINARY, 80),
    GREATER_THAN(Arity.BINARY, 80),
    LESS_THAN_OR_EQUAL_TO(Arity.BINARY, 80),
    GREATER_THAN_OR_EQUAL_TO(Arity.BINARY, 80),
    EQUAL(Arity.BINARY, 80),
    NOT_EQUAL(Arity.BINARY, 80),
    IS(Arity.BINARY, 20),
    IS_NULL(Arity.BINARY, 20),
    IS_NOT_NULL(Arity.BINARY, 20),
    LOGICAL_NEGATION(Arity.UNARY, 12, Associativity.RIGHT),
    LOGICAL_CONJUNCTION(Arity.BINARY, 11, Associativity.LEFT),
    LOGICAL_DISJUNCTION(Arity.BINARY, 10, Associativity.LEFT);

    private final Arity type;
    private final int precedence;
    private final Associativity associativity;

    PostgreSqlOperator(Arity type, int precedence) {
       this(type, precedence, Associativity.NONE);
    }

    PostgreSqlOperator(Arity type, int precedence, Associativity associativity) {
        this.type = type;
        this.precedence = precedence;
        this.associativity = associativity;
    }

    @Override
    public final Arity arity() {
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
