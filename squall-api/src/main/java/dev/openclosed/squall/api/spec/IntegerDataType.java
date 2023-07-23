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
 * Integer data types defined by SQL standard.
 */
public enum IntegerDataType implements DataType {
    INTEGER(Integer.MIN_VALUE, Integer.MAX_VALUE),
    SMALLINT(Short.MIN_VALUE, Short.MAX_VALUE),
    BIGINT(Long.MIN_VALUE, Long.MAX_VALUE);

    private final long minValue;
    private final long maxValue;

    IntegerDataType(long minValue, long maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public String typeName() {
        return name().toLowerCase();
    }

    public long minValue() {
        return minValue;
    }

    public long maxValue() {
        return maxValue;
    }
}
