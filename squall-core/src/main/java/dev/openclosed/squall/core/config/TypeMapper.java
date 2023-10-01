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

package dev.openclosed.squall.core.config;

import dev.openclosed.squall.api.sql.spec.SpecMetadata;

import java.lang.System.Logger.Level;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Type mapper that maps a value to the target type.
 */
enum TypeMapper {
    BOOLEAN {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            return requireType(source, Boolean.class);
        }
    },
    INTEGER {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            return requireType(source, Integer.class);
        }
    },
    LONG {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            return requireType(source, Long.class);
        }
    },
    SHORT {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            return requireType(source, Short.class);
        }
    },
    STRING {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            return requireType(source, String.class);
        }
    },
    ENUM {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            String s = requireType(source, String.class);
            return mapToEnum(s, target);
        }

        private static <T extends Enum<T>> Enum<T> mapToEnum(String source, Class<?> target) {
            assert target.isEnum();
            @SuppressWarnings("unchecked")
            Class<T> enumType = (Class<T>) target;
            try {
                return Enum.valueOf(enumType, source.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new MappingException(bundle -> bundle.ILLEGAL_VALUE(target, source));
            }
        }
    },
    LIST {
        @Override
        protected Object map(Object source, RecordComponent target, MapperContext context) {
            var collection = requireType(source, Collection.class);
            if (collection.isEmpty()) {
                return Collections.emptyList();
            }
            Object[] array = mapToArray(collection, target, context);
            return List.of(array);
        }
    },
    LOCALE {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            String s = requireType(source, String.class);
            return Locale.forLanguageTag(s);
        }
    },
    MAP {
        @Override
        protected Object map(Object source, RecordComponent target, MapperContext context) {
            Map<?, ?> map = requireType(source, Map.class);
            if (map.isEmpty()) {
                return Collections.emptyMap();
            }
            return map(map, target, context);
        }

        private static Map<String, ?> map(Map<?, ?> source, RecordComponent target, MapperContext context) {
            var mapType = (ParameterizedType) target.getGenericType();
            Type[] parameterTypes = mapType.getActualTypeArguments();
            Type parameterType = parameterTypes[1];
            if (parameterType instanceof Class<?> elementClass) {
                return map(source, elementClass, context);
            }
            throw new IllegalStateException();
        }

        private static Map<String, ?> map(Map<?, ?> source, Class<?> elementClass, MapperContext context) {
            elementClass = context.resolveType(elementClass);
            var mapper = findMapper(elementClass);
            var map = new LinkedHashMap<String, Object>();
            boolean failed = false;
            context.push();
            for (var entry : source.entrySet()) {
                context.setCurrent(entry.getKey().toString());
                try {
                    var mappedValue = mapper.map(entry.getValue(), elementClass, context);
                    map.put(entry.getKey().toString(), mappedValue);
                } catch (Exception e) {
                    handleMappingException(e, context);
                    failed = true;
                }
            }
            context.pop();
            if (failed) {
                throw new MappingException();
            }
            return Collections.unmodifiableMap(map);
        }
    },
    /**
     * Target type is {@link Optional}.
     */
    OPTIONAL {
        @Override
        protected Object map(Object source, RecordComponent target, MapperContext context) {
            var parameterizedType = (ParameterizedType) target.getGenericType();
            var parameterType = parameterizedType.getActualTypeArguments()[0];
            if (parameterType instanceof Class<?> targetClass) {
                targetClass = context.resolveType(targetClass);
                var mapper = findMapper(targetClass);
                return Optional.ofNullable(mapper.map(source, targetClass, context));
            }
            throw new IllegalStateException();
        }
    },
    RECORD {
        @Override
        Object map(Object source, Class<?> target, MapperContext context) {
            Map<?, ?> map = requireType(source, Map.class);
            @SuppressWarnings("unchecked")
            var recordTarget = (Class<? extends Record>) target;
            return mapToRecord(map, context.getRecordType(recordTarget), context);
        }

        private static Object mapToRecord(Map<?, ?> source, RecordType<?> target, MapperContext context) {
            var components = target.components();
            Object[] args = new Object[components.length];
            var defaultValues = target.defaulValueMap();

            boolean failed = false;
            int i = 0;
            context.push();
            for (var component : components) {
                var name = component.getName();
                context.setCurrent(name);
                try {
                    args[i++] = mapToComponent(name,
                            source.get(name),
                            component,
                            defaultValues,
                            context);
                } catch (Exception e) {
                    handleMappingException(e, context);
                    failed = true;
                }
            }
            reportUnknownProperties(source.keySet(), defaultValues.keySet(), context);
            context.pop();

            if (failed) {
                throw new MappingException();
            }

            try {
                return target.newInstance(args);
            } catch (ReflectiveOperationException e) {
                throw new MappingException(e);
            }
        }

        private static Object mapToComponent(
            String name,
            Object source,
            RecordComponent component,
            Map<String, Object> defaultValues,
            MapperContext context) {
            if (source != null) {
                var type = context.resolveType(component.getType());
                var mapper = findMapper(type);
                return mapper.map(source, component, context);
            } else if (defaultValues.containsKey(name)) {
                return defaultValues.get(name);
            } else {
                return getDefaultValueForType(component.getType());
            }
        }

        private static void reportUnknownProperties(Set<?> keys, Set<String> components, MapperContext context) {
            keys.stream()
                .filter(key -> !components.contains(key))
                .map(Object::toString)
                .forEach(key -> {
                    context.setCurrent(key);
                    context.addProblem(Level.WARNING, context.messages().UNKNOWN_PROPERTY(key));
                });
        }
    },
    SET {
        @Override
        protected Object map(Object source, RecordComponent target, MapperContext context) {
            var collection = requireType(source, Collection.class);
            if (collection.isEmpty()) {
                return Collections.emptySet();
            }
            Object[] array = mapToArray(collection, target, context);
            return Set.of(array);
        }
    };

    private static final Map<Type, TypeMapper> MAPPERS = buildMapperMap();
    private static final Map<Type, Object> DEFAULT_VALUES = buildDefaultValueMap();

    Object map(Object source, Class<?> target, MapperContext context) {
        return target.cast(source);
    }

    static TypeMapper findMapper(Class<?> target) {
        if (target.isPrimitive()) {
            return getSimpleTypeMapper(target.getName());
        } else if (target.isEnum()) {
            return ENUM;
        } else if (target.isRecord()) {
            return RECORD;
        } else if (MAPPERS.containsKey(target)) {
            return MAPPERS.get(target);
        } else {
            throw new MappingException(bundle -> bundle.UNSUPPORTED_TARGET_TYPE(target));
        }
    }

    static Object getDefaultValueForType(Class<?> target) {
        var value = DEFAULT_VALUES.get(target);
        if (value == null) {
            throw new MappingException(bundle -> bundle.UNSUPPORTED_TARGET_TYPE(target));
        }
        return value;
    }

    protected Object map(Object source, RecordComponent target, MapperContext context) {
        return map(source, target.getType(), context);
    }

    protected Object[] mapToArray(Collection<?> source, RecordComponent rc, MapperContext context) {
        ParameterizedType targetListType = (ParameterizedType) rc.getGenericType();
        Type[] parameterTypes = targetListType.getActualTypeArguments();
        Type parameterType = parameterTypes[0];
        if (parameterType instanceof Class<?> elementType) {
            return mapToArray(source, elementType, context);
        }
        throw new IllegalStateException();
    }

    private Object[] mapToArray(Collection<?> source, Class<?> elementClass, MapperContext context) {
        elementClass = context.resolveType(elementClass);
        var mapper = findMapper(elementClass);
        Object[] array = new Object[source.size()];

        boolean failed = false;
        int index = 0;
        context.push();
        for (var value : source) {
            context.setCurrent(index);
            try {
                var mappedValue = mapper.map(value, elementClass, context);
                array[index++] = mappedValue;
            } catch (Exception e) {
                handleMappingException(e, context);
                failed = true;
            }
        }
        context.pop();

        if (failed) {
            throw new MappingException();
        }

        return array;
    }

    private static <T> T requireType(Object value, Class<T> target) {
        if (!target.isInstance(value)) {
            throw new MappingException(bundle -> bundle.TYPE_MISMATCH(target, value.getClass()));
        }
        return target.cast(value);
    }

    private static TypeMapper getSimpleTypeMapper(String name) {
        return switch (name) {
        case "boolean" -> BOOLEAN;
        case "short" -> SHORT;
        case "int" -> INTEGER;
        case "long" -> LONG;
        default -> throw new IllegalArgumentException(name);
        };
    }

    private static void handleMappingException(Exception e, MapperContext context) {
        if (e instanceof MappingException me) {
            me.getMessage(context.messages()).ifPresent(message -> {
                context.addProblem(Level.ERROR, message);
            });
        }
    }

    private static Map<Type, TypeMapper> buildMapperMap() {
        var map = new HashMap<Type, TypeMapper>();

        map.put(Boolean.class, BOOLEAN);
        map.put(Integer.class, INTEGER);
        map.put(List.class, LIST);
        map.put(Locale.class, LOCALE);
        map.put(Long.class, LONG);
        map.put(Optional.class, OPTIONAL);
        map.put(Map.class, MAP);
        map.put(Set.class, SET);
        map.put(Short.class, SHORT);
        map.put(String.class, STRING);

        map.put(SpecMetadata.class, RECORD);

        return Map.copyOf(map);
    }

    private static Map<Type, Object> buildDefaultValueMap() {
        var map = new HashMap<Type, Object>();
        map.put(Boolean.class, Boolean.FALSE);
        map.put(Optional.class, Optional.empty());
        map.put(List.class, Collections.emptyList());
        map.put(Set.class, Collections.emptySet());
        map.put(Map.class, Collections.emptyMap());
        return Map.copyOf(map);
    }
}
