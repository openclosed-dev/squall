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

/**
 * Data types in the SQL standard.
 */
public enum StandardDataType implements DataType {
    /** Fixed-length bit string type. */
    BIT {
        @Override
        public StandardDataType varying() {
            return BIT_VARYING;
        }
    },
    /** Variable-length bit string type. */
    BIT_VARYING,
    /** Boolean type. */
    BOOLEAN,
    /** Fixed-length character type. */
    CHAR,
    /** Fixed-length character type. */
    CHARACTER {
        @Override
        public StandardDataType varying() {
            return CHARACTER_VARYING;
        }
    },
    /** Variable-length character type. */
    CHARACTER_VARYING,
    /** Date. */
    DATE,
    /** Arbitrary precision numbers. */
    DECIMAL,
    /** Double-precision floating-point type. */
    DOUBLE_PRECISION,
    /** Floating-point type. */
    FLOAT,
    /** Arbitrary precision numbers. */
    NUMERIC,
    /** Single-precision floating-point type. */
    REAL,
    /** Time of day without time zone. */
    TIME {
        @Override
        public StandardDataType withTimeZone() {
            return TIME_WITH_TIME_ZONE;
        }
    },
    /** Time of day with time zone. */
    TIME_WITH_TIME_ZONE,
    /** Date and time without time zone. */
    TIMESTAMP {
        @Override
        public StandardDataType withTimeZone() {
            return TIMESTAMP_WITH_TIME_ZONE;
        }
    },
    /** Date and time with time zone. */
    TIMESTAMP_WITH_TIME_ZONE,
    /** Variable-length character type. */
    VARCHAR;

    private final String typeName;

    StandardDataType() {
        this.typeName = name().replaceAll("_", " ").toLowerCase();
    }

    @Override
    public String typeName() {
        return typeName;
    }

    /**
     * Returns a variation of this data type, which has the specified length.
     * @param length the length of the new data type.
     * @return new data type having the specified length.
     */
    public DataType withLength(int length) {
        return DataType.withLength(this, length);
    }

    /**
     * Returns a variation of this data type, which has the specified precision.
     * @param precision the precision of the new data type.
     * @return new data type having the specified precision.
     */
    public DataType withPrecision(int precision) {
        return DataType.withPrecision(this, precision);
    }

    /**
     * Returns a variation of this data type, which has the specified precision and scale.
     * @param precision the precision of the new data type.
     * @param scale the scale of the new data type.
     * @return new data type having the specified precision and scale.
     */
    public DataType withPrecision(int precision, int scale) {
        return DataType.withPrecision(this, precision, scale);
    }

    /**
     * Returns the variable-length type of this type if exists.
     * @return the variable-length type of this type.
     * @throws UnsupportedOperationException if this type does not have corresponding variable-length type.
     */
    public StandardDataType varying() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a variation of this data type, which has time zone.
     * @return the corresponding data type having time zone.
     * @throws UnsupportedOperationException if this type does not have corresponding type with time zone.
     */
    public StandardDataType withTimeZone() {
        throw new UnsupportedOperationException();
    }
}
