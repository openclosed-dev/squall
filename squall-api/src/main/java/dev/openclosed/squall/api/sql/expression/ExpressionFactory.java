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

package dev.openclosed.squall.api.sql.expression;

import dev.openclosed.squall.api.sql.datatype.DataType;

import java.util.List;
import java.util.Optional;

/**
 * A factory for creating basic expressions.
 */
public interface ExpressionFactory {

    Expression string(String value);

    Expression bitString(String value);

    Expression number(String value);

    FunctionCall functionCall(String name);

    FunctionCall functionCall(String name, List<Expression> arguments);

    Expression unaryOperator(String operator, Expression operand);

    Expression binaryOperator(String operator, Expression left, Expression right);

    Expression typecast(Expression source, DataType dataType);

    Expression is(Expression subject, String predicate);

    Expression in(Expression left, List<Expression> right, boolean negated);

    Expression columnReference(String name);

    Expression caseExpression(
        Optional<Expression> expression,
        List<Expression> when,
        List<Expression> then,
        Optional<Expression> elseExpression);

    /**
     * Creates a sequence manipulation function.
     * @param name the name of the function.
     * @param arguments the arguments of the function.
     * @param sequenceRef the reference to the sequence.
     * @return sequence manipulation function.
     */
    SequenceFunctionCall sequenceFunction(
        String name, List<Expression> arguments, ObjectRef sequenceRef);
}
