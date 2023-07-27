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

import java.util.OptionalInt;

/**
 * An implementation of {@link dev.openclosed.squall.api.spec.Typecast}.
 * @param type
 * @param source
 * @param typeName
 * @param length
 * @param precision
 * @param scale
 */
record Typecast(
    Expression.Type type,
    Expression source,
    String typeName,
    OptionalInt length,
    OptionalInt precision,
    OptionalInt scale
) implements dev.openclosed.squall.api.spec.Typecast, RecordExpression {

    Typecast(Type type, Expression source, String dataType) {
        this(type, source, dataType, OptionalInt.empty(), OptionalInt.empty(), OptionalInt.empty());
    }

    @Override
    public String toString() {
        var sb = new StringBuilder()
            .append("CAST(")
            .append(source.toString())
            .append(" AS ")
            .append(typeName);

        length().ifPresentOrElse(l -> {
            sb.append('(').append(l).append(')');
        }, () -> {
            precision.ifPresent(p -> {
                sb.append('(').append(p);
                scale.ifPresent(s -> {
                    sb.append(", ").append(s);
                });
                sb.append(')');
            });
        });

        return sb.append(')').toString();
    }
}
