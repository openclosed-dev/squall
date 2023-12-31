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

/**
 * Binary operator.
 * @param operator the operator.
 * @param left the left expression of the operator.
 * @param right the right expression of the operator.
 */
public record BinaryOperator(String operator, Expression left, Expression right)
    implements Expression {

    @Override
    public Type type() {
        return Type.BINARY_OPERATOR;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        return new SqlStringBuilder()
            .appendGroupedIfComplex(left)
            .append(' ')
            .append(operator)
            .append(' ')
            .appendGroupedIfComplex(right)
            .toString();
    }
}
