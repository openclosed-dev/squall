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

    default OptionalInt length() {
        return OptionalInt.empty();
    }

    default OptionalInt precision() {
        return OptionalInt.empty();
    }

    default OptionalInt scale() {
        return OptionalInt.empty();
    }

    static DataType withLength(DataType dataType, int length) {
        final var typeName = dataType.typeName();
        return new DataType() {
            @Override
            public String typeName() {
                return typeName;
            }

            @Override
            public OptionalInt length() {
                return OptionalInt.of(length);
            }
        };
    }

    static DataType withPrecision(DataType dataType, int precision) {
        final var typeName = dataType.typeName();
        return new DataType() {
            @Override
            public String typeName() {
                return typeName;
            }

            @Override
            public OptionalInt precision() {
                return OptionalInt.of(precision);
            }
        };
    }

    static DataType withPrecision(DataType dataType, int precision, int scale) {
        final var typeName = dataType.typeName();
        return new DataType() {
            @Override
            public String typeName() {
                return typeName;
            }

            @Override
            public OptionalInt precision() {
                return OptionalInt.of(precision);
            }

            @Override
            public OptionalInt scale() {
                return OptionalInt.of(scale);
            }
        };
    }
}
