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

/**
 * SQL standard data types.
 */
public enum StandardDataType implements DataType {
    BIT {
        @Override
        public StandardDataType varying() {
            return BIT_VARYING;
        }
    },
    BIT_VARYING,
    BOOLEAN,
    CHAR,
    CHARACTER {
        @Override
        public StandardDataType varying() {
            return CHARACTER_VARYING;
        }
    },
    CHARACTER_VARYING,
    DATE,
    DECIMAL,
    DOUBLE_PRECISION,
    FLOAT,
    NUMERIC,
    REAL,
    TIME {
        @Override
        public StandardDataType withTimeZone() {
            return TIME_WITH_TIME_ZONE;
        }
    },
    TIME_WITH_TIME_ZONE,
    TIMESTAMP {
        @Override
        public StandardDataType withTimeZone() {
            return TIMESTAMP_WITH_TIME_ZONE;
        }
    },
    TIMESTAMP_WITH_TIME_ZONE,
    VARCHAR;

    private final String typeName;

    StandardDataType() {
        this.typeName = name().replaceAll("_", " ").toLowerCase();
    }

    @Override
    public String typeName() {
        return typeName;
    }

    public DataType withLength(int length) {
        return DataType.withLength(this, length);
    }

    public DataType withPrecision(int precision) {
        return DataType.withPrecision(this, precision);
    }

    public DataType withPrecision(int precision, int scale) {
        return DataType.withPrecision(this, precision, scale);
    }

    public StandardDataType varying() {
        throw new UnsupportedOperationException();
    }

    public StandardDataType withTimeZone() {
        throw new UnsupportedOperationException();
    }
}
