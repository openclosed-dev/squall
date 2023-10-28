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

import java.util.List;
import java.util.Objects;

/**
 * Negated form of {@link In} expression.
 * @param left the left expression.
 * @param right the right expression.
 */
public record NotIn(Expression left, List<Expression> right) implements Expression {

    /**
     * Creates an instance of a {@code NotIn} record class.
     * @param left the left expression.
     * @param right the right expression.
     */
    public NotIn {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        right = List.copyOf(right);
    }

    @Override
    public Type type() {
        return Type.NOT_IN;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        return In.toSql(left(), "NOT IN", right());
    }
}
