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

import dev.openclosed.squall.api.sql.datatype.DataType;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * Typecast expression.
 * @param source the source expression to be typecasted.
 * @param typeName the name of the type.
 * @param length the maximum length of this type.
 * @param precision the total number of digits, or empty if not specified.
 * @param scale the number of digits after the decimal point.
 */
public record Typecast(
    Expression source,
    String typeName,
    OptionalInt length,
    OptionalInt precision,
    OptionalInt scale) implements Expression, DataType {

    /**
     * Creates a typecast expression.
     * @param source the original expression to be typecasted.
     * @param dataType the data type.
     * @return created expression.
     */
    public static Typecast of(Expression source, DataType dataType) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(dataType);
        return new Typecast(source,
            dataType.typeName(), dataType.length(), dataType.precision(), dataType.scale());
    }

    public Typecast {
        Objects.requireNonNull(source);
        Objects.requireNonNull(typeName);
        Objects.requireNonNull(length);
        Objects.requireNonNull(precision);
        Objects.requireNonNull(scale);
    }

    @Override
    public Type type() {
        return Type.TYPECAST;
    }

    @Override
    public String toSql() {
        var sb = new SqlStringBuilder()
            .append("CAST(")
            .appendGroupedIfComplex(source)
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
