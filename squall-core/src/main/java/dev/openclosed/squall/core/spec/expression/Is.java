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

import java.util.Objects;

public record Is(Type type, Expression subject, String predicate) implements MapSourceExpression {

    public Is(Expression subject, String predicate) {
        this(Type.IS, subject, predicate);
    }

    public Is {
        Objects.requireNonNull(subject);
        Objects.requireNonNull(predicate);
        predicate = predicate.toLowerCase();
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public String toSql() {
        return new SqlStringBuilder()
            .appendGroupedIfComplex(subject())
            .append(' ')
            .append(predicate().replaceAll("_", " ").toUpperCase())
            .toString();
    }
}