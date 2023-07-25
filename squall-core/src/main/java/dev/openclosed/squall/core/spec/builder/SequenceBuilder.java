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

package dev.openclosed.squall.core.spec.builder;

import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.core.spec.DefaultSequence;

import java.util.List;

final class SequenceBuilder extends ComponentBuilder {

    private IntegerDataType dataType = IntegerDataType.BIGINT;
    private long increment = 1L;
    private Long startValue;
    private Long maxValue;
    private Long minValue;

    SequenceBuilder(String name, List<DocAnnotation> annotations) {
        super(name, annotations);
    }

    Sequence build() {
        final long min = computeMinValue();
        final long max = computeMaxValue();
        final long start = computeStart(min, max);
        return new DefaultSequence(name(),
            dataType.typeName(),
            start,
            this.increment,
            max,
            min,
            annotations());
    }

    void setDataType(IntegerDataType dataType) {
        this.dataType = dataType;
    }

    void setStart(long start) {
        this.startValue = start;
    }

    void setIncrement(long increment) {
        this.increment = increment;
    }

    void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    private long computeMaxValue() {
        if (maxValue != null) {
            return maxValue;
        } else if (increment < 0) {
            return -1L;
        } else {
            return dataType.maxValue();
        }
    }

    private long computeMinValue() {
        if (minValue != null) {
            return minValue;
        } else if (increment < 0) {
            return dataType.minValue();
        } else {
            return 1L;
        }
    }

    private long computeStart(long minValue, long maxValue) {
        if (startValue != null) {
            return startValue;
        } else if (increment < 0) {
            return maxValue;
        } else {
            return minValue;
        }
    }
}
