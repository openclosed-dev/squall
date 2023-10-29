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

import dev.openclosed.squall.api.text.json.Property;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * SQL Case expression.
 * @param expression the expression to evaluate, if exists.
 * @param when one or more when clauses.
 * @param elseClause optional else clause.
 */
public record Case(
    Optional<Expression> expression,
    List<When> when,
    @Property("else")
    Optional<Expression> elseClause) implements Expression {

    /**
     * SQL When clause.
     * @param condition the condition or value part of the When clause.
     * @param result the result part of the When clause.
     */
    public record When(Expression condition, Expression result) {

        /**
         * Creates an instance of a {@code When} record class.
         * @param condition the condition or value part of the When clause.
         * @param result the result part of the When clause.
         */
        public When {
            Objects.requireNonNull(condition);
            Objects.requireNonNull(result);
        }
    }

    /**
     * Creates an instance of a {@code Case} record class.
     * @param expression the expression to evaluate, if exists.
     * @param when one or more when clauses.
     * @param elseClause optional else clause.
     */
    public Case {
        Objects.requireNonNull(expression);
        Objects.requireNonNull(when);
        Objects.requireNonNull(elseClause);
        when = List.copyOf(when);
    }

    /**
     * Creates an instance of a {@code Case} record class.
     * @param expression the expression to evaluate, may be {@code null}.
     * @param whenClauses one or more When clauses.
     * @param elseClause the ELSE clause, may be {@code null}.
     */
    public Case(Expression expression, List<When> whenClauses, Expression elseClause) {
        this(Optional.ofNullable(expression), whenClauses, Optional.ofNullable(elseClause));
    }

    @Override
    public Type type() {
        return Type.CASE;
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

        elseClause().ifPresent(e -> sb.append("ELSE ").appendGroupedIfComplex(e).append(' '));

        return sb.append("END").toString();
    }
}
