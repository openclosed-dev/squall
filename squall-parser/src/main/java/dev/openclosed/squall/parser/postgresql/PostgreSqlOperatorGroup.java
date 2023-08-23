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

enum PostgreSqlOperatorGroup implements OperatorGroup {
    POSTFIX_TYPECAST(Arity.BINARY, 1300, Associativity.LEFT),
    UNARY_PLUS_MINUS(Arity.UNARY, 1100, Associativity.RIGHT),
    EXPONENTIATION(Arity.BINARY, 1000, Associativity.LEFT),
    MULTIPLICATION_DIVISION(Arity.BINARY, 900, Associativity.LEFT),
    ADDITION_SUBTRACTION(Arity.BINARY, 800, Associativity.LEFT),
    // other operators here
    // BETWEEN IN LIKE ILIKE SIMILAR
    RANGE_MEMBERSHIP(Arity.BINARY, 600),
    COMPARISON(Arity.BINARY, 500),
    IS(Arity.BINARY, 400),
    LOGICAL_NEGATION(Arity.UNARY, 300, Associativity.RIGHT),
    LOGICAL_CONJUNCTION(Arity.BINARY, 200, Associativity.LEFT),
    LOGICAL_DISJUNCTION(Arity.BINARY, 100, Associativity.LEFT);

    private final Arity type;
    private final int precedence;
    private final Associativity associativity;

    PostgreSqlOperatorGroup(Arity type, int precedence) {
       this(type, precedence, Associativity.NONE);
    }

    PostgreSqlOperatorGroup(Arity type, int precedence, Associativity associativity) {
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
