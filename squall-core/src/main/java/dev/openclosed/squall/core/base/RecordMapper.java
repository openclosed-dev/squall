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

package dev.openclosed.squall.core.base;

import dev.openclosed.squall.api.base.MapSource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

final class RecordMapper {

    static Map<String, Object> toMap(Record rec) {
        return convert(rec);
    }

    private static Object convertValue(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Long longValue) {
            return convert(longValue);
        } else if (value instanceof Optional<?> optional) {
            return convert(optional);
        } else if (value instanceof OptionalInt optional) {
            return convert(optional);
        } else if (value instanceof MapSource mapSource) {
            return mapSource.toMap();
        } else if (value instanceof Map<?, ?> map) {
            return convert(map);
        } else if (value instanceof List<?> list) {
            return convert(list);
        } else if (value instanceof Set<?> set) {
            return convert(set);
        } else if (value instanceof Record rec) {
            return convert(rec);
        } else if (value instanceof Enum<?> enumValue) {
            return enumValue.name().toLowerCase();
        }
        return value;
    }

    private static Map<String, Object> convert(Record rec) {
        try {
            return convertOrThrow(rec);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Object> convertOrThrow(Record rec) throws ReflectiveOperationException {
        var map = new LinkedHashMap<String, Object>();
        for (var component : rec.getClass().getRecordComponents()) {
            var accessor = component.getAccessor();
            accessor.setAccessible(true);
            var original = accessor.invoke(rec);
            var converted = convertValue(original);
            if (shouldInclude(converted)) {
                map.put(component.getName(), converted);
            }
        }
        return map;
    }

    private static Number convert(Long value) {
        if (value == null) {
            return null;
        }
        long longValue = value.longValue();
        if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
            return Integer.valueOf((int) longValue);
        } else {
            return value;
        }
    }

    private static Object convert(Optional<?> optional) {
        return optional.map(RecordMapper::convertValue).orElse(null);
    }

    private static Object convert(OptionalInt optional) {
        if (optional.isPresent()) {
            return optional.getAsInt();
        } else {
            return null;
        }
    }

    private static List<Object> convert(Collection<?> list) {
        return list.stream().map(RecordMapper::convertValue).toList();
    }

    private static Map<String, Object> convert(Map<?, ?> map) {
        var newMap = new LinkedHashMap<String, Object>();
        map.forEach((key, value) -> {
            var newValue = convertValue(value);
            if (shouldInclude(newValue)) {
                newMap.put(key.toString(), newValue);
            }
        });
        return newMap;
    }

    private static boolean shouldInclude(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof Collection<?> collection) {
            return !collection.isEmpty();
        }
        return true;
    }

    private RecordMapper() {
    }
}
