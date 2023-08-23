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

package dev.openclosed.squall.api.spec;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * A factory for creating basic expressions.
 */
public interface ExpressionFactory {

    /**
     * Create a new instance of this type.
     * @return newly created instance.
     * @throws java.util.NoSuchElementException if the creation failed.
     */
    static ExpressionFactory newFactory() {
        return ServiceLoader.load(ExpressionFactory.class).findFirst().get();
    }

    Expression string(String value);

    Expression bitString(String value);

    Expression number(String value);

    Expression functionCall(String name);

    Expression functionCall(String name, List<Expression> arguments);

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
}
