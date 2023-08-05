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

import dev.openclosed.squall.api.spec.Expression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Case(
    Type type,
    Optional<Expression> expression,
    List<When> when,
    Optional<Expression> elseResult) implements MapSourceExpression {

    public record When(Expression condition, Expression result) {
        public When {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(result);
        }
    };

    public Case(Expression expression, List<When> when, Expression elseResult) {
        this(Type.CASE, Optional.ofNullable(expression), when, Optional.ofNullable(elseResult));
    }

    public Case {
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

        elseResult().ifPresent(result -> {
            sb.append("ELSE ").appendGroupedIfComplex(result).append(' ');
        });
        return sb.append("END").toString();
    }
}