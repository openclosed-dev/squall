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

package dev.openclosed.squall.api.sql.datatype;

import java.util.Objects;
import java.util.OptionalInt;

/**
 * SQL data type with optional parameters.
 */
public interface DataType {

    /**
     * Returns the name of the data type.
     * @return the name of the data type, always in lower case.
     */
    String typeName();

    /**
     * Returns the maximum length of this type.
     * @return the maximum length of this type, or empty if length is not specified.
     */
    default OptionalInt length() {
        return OptionalInt.empty();
    }

    /**
     * Returns the total number of digits in this type.
     * @return the total number of digits, or empty if precision is not specified.
     */
    default OptionalInt precision() {
        return OptionalInt.empty();
    }

    /**
     * Returns the number of digits after the decimal point.
     * @return the number of digits after the decimal point, or empty if scale is not specified.
     */
    default OptionalInt scale() {
        return OptionalInt.empty();
    }

    /**
     * Returns whether this type has any parameters or not.
     * @return {@code} true if this type has parameters, {@code false} otherwise.
     */
    default boolean isParameterized() {
        return length().isPresent() || precision().isPresent();
    }

    /**
     * Returns the SQL notation of this data type.
     * @return the SQL notation of this data type.
     */
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

    /**
     * Creates a data type having the specified length.
     * @param baseDataType the base data type.
     * @param length the length of the new data type.
     * @return data type having the specified length.
     */
    static DataType withLength(DataType baseDataType, int length) {
        Objects.requireNonNull(baseDataType);
        record StringDataType(String typeName, OptionalInt length) implements DataType { }
        return new StringDataType(baseDataType.typeName(), OptionalInt.of(length));
    }

    /**
     * Creates a data type having the specified precision.
     * @param baseDataType the base data type.
     * @param precision the precision of the new data type.
     * @return data type having the specified precision.
     */
    static DataType withPrecision(DataType baseDataType, int precision) {
        Objects.requireNonNull(baseDataType);
        record NumericDataType(String typeName, OptionalInt precision) implements DataType { }
        return new NumericDataType(baseDataType.typeName(), OptionalInt.of(precision));
    }

    /**
     * Creates a data type having the specified precision and scale.
     * @param baseDataType the base data type.
     * @param precision the precision of the new data type.
     * @param scale the scale of the new data type.
     * @return data type having the specified precision and scale.
     */
    static DataType withPrecision(DataType baseDataType, int precision, int scale) {
        Objects.requireNonNull(baseDataType);
        record NumericDataType(String typeName, OptionalInt precision, OptionalInt scale) implements DataType { }
        return new NumericDataType(
            baseDataType.typeName(),
            OptionalInt.of(precision),
            OptionalInt.of(scale));
    }
}
