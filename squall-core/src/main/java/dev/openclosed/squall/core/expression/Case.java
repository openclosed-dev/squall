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
import dev.openclosed.squall.core.base.Property;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

record Case(
    Type type,
    Optional<Expression> expression,
    List<When> when,
    @Property("else")
    Optional<Expression> elseExpression) implements MapSourceExpression {

    record When(Expression condition, Expression result) {

        When {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(result);
        }
    }

    Case(Optional<Expression> expression, List<When> when, Optional<Expression> elseExpression) {
        this(Type.CASE, expression, when, elseExpression);
    }

    Case {
        when = List.copyOf(when);
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        var sb = new SqlStringBuilder();
        sb.append("CASE ");
        expression().ifPresent(e -> sb.appendGroupedIfComplex(e).append(' '));

        for (var entry : when()) {
            sb.append("WHEN ")
                .appendGroupedIfComplex(entry.condition())
                .append(" THEN ")
                .appendGroupedIfComplex(entry.result())
                .append(' ');
        }

        elseExpression().ifPresent(e -> {
            sb.append("ELSE ").appendGroupedIfComplex(e).append(' ');
        });
        return sb.append("END").toString();
    }
}
