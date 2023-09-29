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

package dev.openclosed.squall.core.expression;

import dev.openclosed.squall.api.expression.Expression;

import java.util.List;

record In(
    Type type,
    Expression left,
    List<Expression> right
    ) implements MapSourceExpression {

    In(Expression left, List<Expression> right, boolean negated) {
        this(negated ? Type.NOT_IN : Type.IN, left, right);
    }

    In {
        if (type != Type.IN && type != Type.NOT_IN) {
            throw new IllegalArgumentException();
        }
        right = List.copyOf(right);
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        var sb = new SqlStringBuilder()
            .appendGroupedIfComplex(left())
            .append(' ')
            .append(type() == Type.IN ? "IN" : "NOT IN")
            .append(" (");

        int i = 0;
        for (var e : right()) {
            if (i++ > 0) {
                sb.append(", ");
            }
            sb.append(e);
        }
        sb.append(')');
        return sb.toString();
    }
}
