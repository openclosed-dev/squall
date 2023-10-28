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
 * SQL In expression.
 * @param left the left expression.
 * @param right the right expression.
 */
public record In(Expression left, List<Expression> right) implements Expression {

    /**
     * Creates an In expression.
     * @param left the left expression.
     * @param right the right expression.
     * @param negated {@code true} for creating No-In expression.
     * @return created expression.
     */
    public static Expression of(Expression left, List<Expression> right, boolean negated) {
        if (negated) {
            return new NotIn(left, right);
        } else {
            return new In(left, right);
        }
    }

    /**
     * Creates an instance of a {@code In} record class.
     * @param left the left expression.
     * @param right the right expression.
     */
    public In {
        Objects.requireNonNull(left);
        Objects.requireNonNull(right);
        right = List.copyOf(right);
    }

    @Override
    public Type type() {
        return Type.IN;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        return toSql(left(), "IN", right());
    }

    static String toSql(Expression left, String operator, List<Expression> right) {
        var sb = new SqlStringBuilder()
            .appendGroupedIfComplex(left)
            .append(' ')
            .append(operator)
            .append(" (");

        int i = 0;
        for (var e : right) {
            if (i++ > 0) {
                sb.append(", ");
            }
            sb.append(e);
        }
        sb.append(')');
        return sb.toString();
    }
}
