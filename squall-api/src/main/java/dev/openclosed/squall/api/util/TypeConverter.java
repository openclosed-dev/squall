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

import dev.openclosed.squall.api.base.Property;
import dev.openclosed.squall.api.sql.expression.Expression;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

enum TypeConverter {
    LONG {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Long;
        }

        @Override
        Number convert(Object value) {
            long longValue = ((Long) value).longValue();
            if (longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE) {
                return Integer.valueOf((int) longValue);
            } else {
                return longValue;
            }
        }
    },
    ENUM {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Enum<?>;
        }

        @Override
        String convert(Object value) {
            return ((Enum<?>) value).name().toLowerCase();
        }
    },
    OPTIONAL {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Optional<?>;
        }

        @Override
        Object convert(Object value) {
            Optional<?> optional = (Optional<?>) value;
            return optional.map(TypeConverter::convertUnknown)
                .orElse(null);
        }
    },
    OPTIONAL_INT {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof OptionalInt;
        }

        @Override
        Object convert(Object value) {
            var optional = (OptionalInt) value;
            if (optional.isPresent()) {
                return optional.getAsInt();
            } else {
                return null;
            }
        }
    },
    MAP {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Map<?, ?>;
        }

        @Override
        Map<String, Object> convert(Object value) {
            return convertMap((Map<?, ?>) value);
        }

        static Map<String, Object> convertMap(Map<?, ?> map) {
            var newMap = new LinkedHashMap<String, Object>();
            map.forEach((key, value) -> {
                var newValue = convertUnknown(value);
                if (shouldInclude(newValue)) {
                    newMap.put(key.toString(), newValue);
                }
            });
            return Collections.unmodifiableMap(newMap);
        }
    },
    COLLECTION {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Collection<?>;
        }

        @Override
        List<?> convert(Object value) {
            var collection = (Collection<?>) value;
            return collection.stream().map(TypeConverter::convertUnknown).toList();
        }
    },
    EXPRESSION {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Expression;
        }

        Map<String, Object> convert(Object value) {
            var newMap = new LinkedHashMap<String, Object>();
            var expression = (Expression) value;
            newMap.put("type", expression.type().name().toLowerCase());
            if (value instanceof Record rec) {
                convertRecordToMap(rec, newMap);
            }
            return Collections.unmodifiableMap(newMap);
        }
    },
    RECORD {
        @Override
        boolean isTypeOf(Object value) {
            return value instanceof Record;
        }

        Map<String, Object> convert(Object value) {
            var map = new LinkedHashMap<String, Object>();
            convertRecordToMap((Record) value, map);
            return Collections.unmodifiableMap(map);
        }
    };

    static final List<TypeConverter> CONVERTERS = List.of(values());

    boolean isTypeOf(Object value) {
        return false;
    }

    Object convert(Object value) {
        return value;
    }

    static Object convertUnknown(Object value) {
        if (value == null) {
            return null;
        }
        for (var converter : CONVERTERS) {
            if (converter.isTypeOf(value)) {
                return converter.convert(value);
            }
        }
        return value;
    }

    static boolean shouldInclude(Object value) {
        if (value == null) {
            return false;
        } else if (Boolean.FALSE.equals(value)) {
            return false;
        } else if (value instanceof Collection<?> collection) {
            return !collection.isEmpty();
        }
        return true;
    }

    // For record

    static void convertRecordToMap(Record rec, Map<String, Object> newMap) {
        try {
            for (var component : rec.getClass().getRecordComponents()) {
                var prop = component.getAnnotation(Property.class);
                var accessor = component.getAccessor();
                accessor.setAccessible(true);
                var original = accessor.invoke(rec);
                var converted = convertUnknown(original);
                if ((prop != null && !prop.omit()) || shouldInclude(converted)) {
                    String propertyName = (prop != null) ? prop.value() : component.getName();
                    newMap.put(propertyName, converted);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
