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

package dev.openclosed.squall.parser.basic;

import dev.openclosed.squall.api.spec.DataType;

enum IntervalDataType implements DataType {
    INTERVAL {
        @Override
        IntervalDataType from(Keyword field) {
            return switch (field.standard()) {
                case YEAR -> INTERVAL_YEAR;
                case MONTH -> INTERVAL_MONTH;
                case DAY -> INTERVAL_DAY;
                case HOUR -> INTERVAL_HOUR;
                case MINUTE -> INTERVAL_MINUTE;
                case SECOND -> INTERVAL_SECOND;
                default -> this;
            };
        }
    },
    INTERVAL_YEAR {
        @Override
        IntervalDataType to(Keyword field) {
            return switch (field.standard()) {
                case MONTH -> INTERVAL_YEAR_TO_MONTH;
                default -> throw badFieldException(field);
            };
        }
    },
    INTERVAL_MONTH,
    INTERVAL_DAY {
        @Override
        IntervalDataType to(Keyword field) {
            return switch (field.standard()) {
                case HOUR -> INTERVAL_DAY_TO_HOUR;
                case MINUTE -> INTERVAL_DAY_TO_MINUTE;
                case SECOND -> INTERVAL_DAY_TO_SECOND;
                default -> throw badFieldException(field);
            };
        }
    },
    INTERVAL_HOUR {
        @Override
        IntervalDataType to(Keyword field) {
            return switch (field.standard()) {
                case MINUTE -> INTERVAL_HOUR_TO_MINUTE;
                case SECOND -> INTERVAL_HOUR_TO_SECOND;
                default -> throw badFieldException(field);
            };
        }
    },
    INTERVAL_MINUTE {
        @Override
        IntervalDataType to(Keyword field) {
            return switch (field.standard()) {
                case SECOND -> INTERVAL_MINUTE_TO_SECOND;
                default -> throw badFieldException(field);
            };
        }
    },
    INTERVAL_SECOND,
    INTERVAL_YEAR_TO_MONTH,
    INTERVAL_DAY_TO_HOUR,
    INTERVAL_DAY_TO_MINUTE,
    INTERVAL_DAY_TO_SECOND,
    INTERVAL_HOUR_TO_MINUTE,
    INTERVAL_HOUR_TO_SECOND,
    INTERVAL_MINUTE_TO_SECOND;

    private final String typeName;

    IntervalDataType() {
        this.typeName = name().replaceAll("_", " ").toLowerCase();
    }

    @Override
    public String typeName() {
        return typeName;
    }

    IntervalDataType from(Keyword field) {
        return this;
    }

    IntervalDataType to(Keyword field) {
        throw badFieldException(field);
    }

    DataType withPrecision(int precision) {
        return DataType.withPrecision(this, precision);
    }

    private static IllegalArgumentException badFieldException(Keyword field) {
        return new IllegalArgumentException(field.canonicalName());
    }
}
