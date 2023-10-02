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

package dev.openclosed.squall.core.sql.expression;

import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.expression.ExpressionFactory;
import dev.openclosed.squall.api.sql.expression.ObjectRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Default implementation of {@link ExpressionFactory}.
 */
public final class DefaultExpressionFactory implements ExpressionFactory {

    @Override
    public Expression string(String value) {
        if (value.isEmpty()) {
            return EmptyStringLiteral.EMPTY;
        } else {
            return new StringLiteral(Expression.Type.STRING, value);
        }
    }

    @Override
    public Expression bitString(String value) {
        return new BitStringLiteral(Expression.Type.BIT_STRING, value);
    }

    @Override
    public Expression number(String value) {
        return new NumberLiteral(Expression.Type.NUMBER, value);
    }

    @Override
    public SimpleFunctionCall functionCall(String name) {
        return new SimpleFunctionCall(Expression.Type.FUNCTION, name);
    }

    @Override
    public FunctionCall functionCall(String name, List<Expression> arguments) {
        return new FunctionCall(Expression.Type.FUNCTION, name, List.copyOf(arguments));
    }

    @Override
    public Expression unaryOperator(String operator, Expression operand) {
        return new UnaryOperator(Expression.Type.UNARY_OPERATOR, operator, operand);
    }

    @Override
    public Expression binaryOperator(String operator, Expression left, Expression right) {
        return new BinaryOperator(Expression.Type.BINARY_OPERATOR, operator, left, right);
    }

    @Override
    public Expression typecast(Expression source, DataType dataType) {
        return new Typecast(source, dataType);
    }

    @Override
    public Expression is(Expression subject, String predicate) {
        return new Is(subject, predicate);
    }

    @Override
    public Expression in(Expression left, List<Expression> right, boolean negated) {
        return new In(left, right, negated);
    }

    @Override
    public Expression columnReference(String name) {
        return new ColumnReference(name);
    }

    @Override
    public Expression caseExpression(
        Optional<Expression> expression,
        List<Expression> when,
        List<Expression> then,
        Optional<Expression> elseExpression) {
        if (when.size() != then.size()) {
            throw new IllegalArgumentException();
        }
        final int size = when.size();
        List<Case.When> zipped = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            zipped.add(new Case.When(when.get(i), then.get(i)));
        }
        return new Case(expression, zipped, elseExpression);
    }

    @Override
    public SequenceFunctionCall sequenceFunction(
        String name, List<Expression> arguments, ObjectRef sequenceRef) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(arguments);
        Objects.requireNonNull(sequenceRef);

        return new SequenceFunctionCall(
            Expression.Type.SEQUENCE_FUNCTION,
            name,
            arguments,
            sequenceRef.toList());
    }
}
