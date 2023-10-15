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

package dev.openclosed.squall.api.util;

import java.util.Map;
import java.util.Objects;

/**
 * Utility class for records.
 */
public final class Records {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object object) {
        Objects.requireNonNull(object);
        var converted = TypeConverter.convertUnknown(object);
        if (converted instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        } else {
            throw new IllegalArgumentException("Not a map");
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Record rec) {
        Objects.requireNonNull(rec);
        return (Map<String, Object>) TypeConverter.RECORD.convert(rec);
    }

    private Records() {
    }
}
