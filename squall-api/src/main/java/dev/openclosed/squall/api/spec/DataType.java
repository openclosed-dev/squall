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

package dev.openclosed.squall.api.spec;

import java.util.OptionalInt;

/**
 * SQL data type with length, precision, or scale.
 */
public interface DataType {

    /**
     * Returns the name of the data type.
     * @return the name of the data type.
     */
    String typeName();

    /**
     * Returns the maximum length of this type.
     * @return the maximum length of this type, or empty if not specified.
     */
    default OptionalInt length() {
        return OptionalInt.empty();
    }

    /**
     * Returns the total number of digits in this type.
     * @return the total number of digits, or empty if not specified.
     */
    default OptionalInt precision() {
        return OptionalInt.empty();
    }

    /**
     * Returns the number of digits after the decimal point.
     * @return the number of digits after the decimal point, or empty if not specified.
     */
    default OptionalInt scale() {
        return OptionalInt.empty();
    }

    default boolean isParameterized() {
        return length().isPresent() || precision().isPresent();
    }

    default String toSqlType() {
        return isParameterized() ? toParameterizedSqlType() : typeName();
    }

    private String toParameterizedSqlType() {
        var builder = new StringBuilder(typeName()).append('(');
        length().ifPresentOrElse(builder::append, () -> {
            precision().ifPresent(precision -> {
                builder.append(precision);
                scale().ifPresent(scale -> builder.append(", ").append(scale));
            });
        });
        return builder.append(')').toString();
    }

    static DataType withLength(DataType dataType, int length) {
        record StringDataType(String typeName, OptionalInt length) implements DataType { }
        return new StringDataType(dataType.typeName(), OptionalInt.of(length));
    }

    static DataType withPrecision(DataType dataType, int precision) {
        record NumericDataType(String typeName, OptionalInt precision) implements DataType { }
        return new NumericDataType(dataType.typeName(), OptionalInt.of(precision));
    }

    static DataType withPrecision(DataType dataType, int precision, int scale) {
        record NumericDataType(String typeName, OptionalInt precision, OptionalInt scale) implements DataType { }
        return new NumericDataType(
            dataType.typeName(),
            OptionalInt.of(precision),
            OptionalInt.of(scale));
    }
}
