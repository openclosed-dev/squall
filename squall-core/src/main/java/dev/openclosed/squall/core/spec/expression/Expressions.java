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

package dev.openclosed.squall.core.spec.expression;

import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.Expression;

import java.util.List;

public final class Expressions {

    public static Expression createString(String value) {
        if (value.isEmpty()) {
            return EmptyStringLiteral.EMPTY;
        } else {
            return new StringLiteral(Expression.Type.STRING, value);
        }
    }

    public static Expression createBitString(String value) {
        return new BitStringLiteral(Expression.Type.BIT_STRING, value);
    }

    public static Expression createNumber(String value) {
        return new NumberLiteral(Expression.Type.NUMBER, value);
    }

    public static Expression createFunctionCall(String name) {
        return new SimpleFunctionCall(Expression.Type.FUNCTION, name);
    }

    public static Expression createFunctionCall(String name, List<Expression> arguments) {
        return new FunctionCall(Expression.Type.FUNCTION, name, List.copyOf(arguments));
    }

    public static Expression createUnaryOperator(String operator, Expression operand) {
        return new UnaryOperator(Expression.Type.UNARY_OPERATOR, operator, operand);
    }

    public static Expression createBinaryOperator(String operator, Expression left, Expression right) {
        return new BinaryOperator(Expression.Type.BINARY_OPERATOR, operator, left, right);
    }

    public static Expression createColumnReference(String name) {
        return new ColumnReference(Expression.Type.COLUMN_REFERENCE, name);
    }

    public static Expression createTypecast(Expression source, DataType dataType) {
        return new Typecast(Expression.Type.TYPECAST,
            source,
            dataType.typeName(),
            dataType.length(),
            dataType.precision(),
            dataType.scale());
    }

    public static Expression createIsPredicate(Expression subject, String predicate) {
        return new Is(Expression.Type.IS, subject, predicate.toLowerCase());
    }

    public static Expression createInPredicate(Expression left, List<Expression> right, boolean negated) {
        var type = negated ? Expression.Type.NOT_IN : Expression.Type.IN;
        return new In(type, left, right);
    }

    private Expressions() {
    }
}

