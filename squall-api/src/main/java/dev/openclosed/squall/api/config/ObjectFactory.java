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

package dev.openclosed.squall.api.config;

import dev.openclosed.squall.api.text.json.JsonPointer;
import dev.openclosed.squall.api.message.Message;
import dev.openclosed.squall.api.text.Problem;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.RecordComponent;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

class ObjectFactory {

    private final MessageBundle messageBundle;
    private final TypeResolver typeResolver;

    ObjectFactory(MessageBundle messageBundle) {
        this(messageBundle, TypeResolver.builder().build());
    }

    ObjectFactory(MessageBundle messageBundle, TypeResolver typeResolver) {
        this.messageBundle = messageBundle;
        this.typeResolver = typeResolver;
    }

    <T extends Record> T createRecord(
        Map<String, ?> map,
        Class<T> targetClass,
        Consumer<Problem> problemHandler) {

        var context = new Context(
            this.messageBundle,
            this.typeResolver,
            problemHandler
        );
        try {
            return TypeConverter.RECORD.convert(map, targetClass, context);
        } catch (Exception e) {
            handleException(e, context);
            throw e;
        }
    }

    static class Context {
        private final MessageBundle messageBundle;
        private final TypeResolver typeResolver;
        private final Consumer<Problem> problemHandler;
        private final List<String> tokens = new ArrayList<>();
        private final HashMap<Class<? extends Record>, RecordType<?>> recordTypeCache = new HashMap<>();

        Context(
            MessageBundle messageBundle,
            TypeResolver typeResolver,
            Consumer<Problem> problemHandler
            ) {
            this.messageBundle = messageBundle;
            this.typeResolver = typeResolver;
            this.problemHandler = problemHandler;
        }

        MessageBundle messageBundle() {
            return this.messageBundle;
        }

        <T extends Record> RecordType<T> getRecordType(Class<T> targetClass) {
            @SuppressWarnings("unchecked")
            var recordType = (RecordType<T>) recordTypeCache.computeIfAbsent(targetClass, RecordType::of);
            return recordType;
        }

        Class<?> resolveType(Class<?> type) {
            return typeResolver.resolveType(type);
        }

        void push() {
            tokens.add(null);
        }

        void setCurrent(String name) {
            tokens.set(tokens.size() - 1, name);
        }

        void setCurrent(int index) {
            tokens.set(tokens.size() - 1, String.valueOf(index));
        }

        void pop() {
            tokens.remove(tokens.size() - 1);
        }

        void addProblem(System.Logger.Level severity, Message message) {
            var problem = new ConfigProblem(severity, message, JsonPointer.of(tokens));
            problemHandler.accept(problem);
        }
    }

    /**
     * Converters classified by target type.
     */
    enum TypeConverter {
        BOOLEAN(Boolean.class, Boolean.FALSE),
        INTEGER(Integer.class, 0),
        LONG(Long.class, 0L) {
            @Override
            Object convertNonNull(Object source, Class<?> targetClass, Context context) {
                if (source instanceof Integer i) {
                    return i.longValue();
                }
                return super.convertNonNull(source, targetClass, context);
            }
        },
        SHORT(Short.class, (short) 0) {
            @Override
            Object convertNonNull(Object source, Class<?> targetClass, Context context) {
                if (source instanceof Integer i) {
                    return i.shortValue();
                }
                return super.convertNonNull(source, targetClass, context);
            }
        },
        STRING(String.class, ""),
        LOCALE(Locale.class, Locale.getDefault()) {
            @Override
            Locale convertNonNull(Object source, Class<?> targetClass, Context context) {
                String languageTag = requireType(source, String.class);
                return Locale.forLanguageTag(languageTag);
            }
        },
        ENUM(Enum.class) {
            @Override
            Enum<?> convertNonNull(Object source, Class<?> targetClass, Context context) {
                String value = requireType(source, String.class);
                return convertToEnum(value, targetClass);
            }

            static <T extends Enum<T>> T convertToEnum(String source, Class<?> targetClass) {
                @SuppressWarnings("unchecked")
                var enumType = (Class<T>) requireEnum(targetClass);
                try {
                    return Enum.valueOf(enumType, source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new ConversionException(bundle -> bundle.ILLEGAL_VALUE(targetClass, source));
                }
            }
        },
        OPTIONAL(Optional.class, Optional.empty()) {
            @Override
            Optional<?> convertNonNull(Object source, RecordComponent component, Context context) {
                var parameterizedType = (ParameterizedType) component.getGenericType();
                var parameterType = parameterizedType.getActualTypeArguments()[0];
                if (!(parameterType instanceof Class<?> targetClass)) {
                    throw new IllegalStateException();
                }
                var resolvedClass = context.resolveType(targetClass);
                var converter = getConverter(resolvedClass, context);
                return Optional.ofNullable(converter.convert(source, resolvedClass, context));
            }
        },
        LIST(List.class, Collections.emptyList()) {
            @Override
            List<?> convertNonNull(Object source, RecordComponent component, Context context) {
                var collection = requireType(source, Collection.class);
                if (collection.isEmpty()) {
                    return Collections.emptyList();
                }
                Object[] array = convertToArray(collection, component, context);
                return List.of(array);
            }
        },
        SET(Set.class, Collections.emptySet()) {
            @Override
            Set<?> convertNonNull(Object source, RecordComponent component, Context context) {
                var collection = requireType(source, Collection.class);
                if (collection.isEmpty()) {
                    return Collections.emptySet();
                }
                Object[] array = convertToArray(collection, component, context);
                var orderedSet = new LinkedHashSet<>(array.length);
                Collections.addAll(orderedSet, array);
                return Collections.unmodifiableSet(orderedSet);
            }
        },
        MAP(Map.class, Collections.emptyMap()) {
            @Override
            Map<?, ?> convertNonNull(Object source, RecordComponent component, Context context) {
                Map<?, ?> map = requireType(source, Map.class);
                if (map.isEmpty()) {
                    return Collections.emptyMap();
                }
                return convertToMap(map, component, context);
            }

            static Map<?, ?> convertToMap(Map<?, ?> source, RecordComponent component, Context context) {
                var mapType = (ParameterizedType) component.getGenericType();
                Type[] parameterTypes = mapType.getActualTypeArguments();
                Type parameterType = parameterTypes[1];
                if (!(parameterType instanceof Class<?> valueType)) {
                    throw new IllegalStateException();
                }
                return convertToMap(source, valueType, context);
            }

            static Map<String, ?> convertToMap(Map<?, ?> source, Class<?> valueType, Context context) {
                var converter = getConverter(valueType, context);
                var map = new LinkedHashMap<String, Object>();

                boolean failed = false;
                context.push();
                for (var entry : source.entrySet()) {
                    var key = entry.getKey().toString();
                    context.setCurrent(key);
                    try {
                        map.put(key, converter.convert(entry.getValue(), valueType, context));
                    } catch (Exception e) {
                        handleException(e, context);
                        failed = true;
                    }
                }
                context.pop();

                if (failed) {
                    throw new RuntimeException();
                }

                return Collections.unmodifiableMap(map);
            }
        },
        RECORD(Record.class) {
            @Override
            Record convertNonNull(Object source, Class<?> targetClass, Context context) {
                Map<?, ?> mapSource = requireType(source, Map.class);
                var targetRecord = requireRecord(targetClass);
                return convertToRecord(mapSource, targetRecord, context);
            }

            static <T extends Record> T convertToRecord(
                Map<?, ?> source, Class<T> targetRecord, Context context) {
                return convertToRecord(source, context.getRecordType(targetRecord), context);
            }

            static <T extends Record> T convertToRecord(Map<?, ?> source, RecordType<T> recordType, Context context) {

                var components = recordType.components();
                Object[] args = new Object[components.length];

                boolean failed = false;
                int index = 0;
                context.push();
                for (var component : components) {
                    var componentName = component.getName();
                    context.setCurrent(componentName);
                    try {
                        var converted = convertToComponent(
                            source.get(componentName),
                            component,
                            recordType,
                            context
                        );
                        args[index++] = converted;
                    } catch (Exception e) {
                        handleException(e, context);
                        failed = true;
                    }
                }
                reportUnknownProperties(source.keySet(), recordType, context);
                context.pop();

                if (failed) {
                    throw new RuntimeException("convertToRecord() failed");
                }

                try {
                    return recordType.newInstance(args);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            }

            static Object convertToComponent(
                Object source,
                RecordComponent component,
                RecordType<?> recordType,
                Context context
            ) {
                if (source == null) {
                    var defaultValues = recordType.defaulValueMap();
                    if (defaultValues.containsKey(component.getName())) {
                        return defaultValues.get(component.getName());
                    }
                }
                var converter = getConverter(component.getType(), context);
                return converter.convert(source, component, context);
            }

            static void reportUnknownProperties(Set<?> keys, RecordType<?> recordType, Context context) {
                keys.stream()
                    .filter(key -> !recordType.containsKey(key))
                    .map(Object::toString)
                    .forEach(key -> {
                        context.setCurrent(key);
                        context.addProblem(System.Logger.Level.WARNING, context.messageBundle.UNKNOWN_PROPERTY(key));
                    });
            }
        };

        private static final Map<Class<?>, TypeConverter> CONVERTER_MAP = buildConverterMap();

        private final Class<?> targetClass;
        private final Object defaultValue;

        TypeConverter(Class<?> targetClass) {
            this(targetClass, null);
        }

        TypeConverter(Class<?> targetClass, Object defaultValue) {
            this.targetClass = targetClass;
            this.defaultValue = defaultValue;
        }

        final <T> T convert(Object source, Class<T> targetClass, Context context) {
            if (source == null) {
                return convertNull(targetClass);
            } else {
                @SuppressWarnings("unchecked")
                T converted = (T) convertNonNull(source, targetClass, context);
                return converted;
            }
        }

        final Object convert(Object source, RecordComponent component, Context context) {
            if (source == null) {
                return convertNull(component.getType());
            } else {
                return convertNonNull(source, component, context);
            }
        }

        final <T> T convertNull(Class<T> targetClass) {
            if (this.defaultValue == null) {
                throw new ConversionException(bundle -> bundle.UNSUPPORTED_TARGET_TYPE(targetClass));
            }
            @SuppressWarnings("unchecked")
            var converted = (T) this.defaultValue;
            return converted;
        }

        Object convertNonNull(Object source, Class<?> targetClass, Context context) {
            return requireType(source, this.targetClass);
        }

        Object convertNonNull(Object source, RecordComponent component, Context context) {
            return convertNonNull(source, component.getType(), context);
        }

        //

        static Object[] convertToArray(Collection<?> source, Class<?> elementClass, Context context) {
            var converter = getConverter(elementClass, context);
            Object[] array = new Object[source.size()];

            boolean failed = false;
            int index = 0;
            context.push();
            for (var entry : source) {
                context.setCurrent(index);
                try {
                    array[index++] = converter.convert(entry, elementClass, context);
                } catch (Exception e) {
                    handleException(e, context);
                    failed = true;
                }
            }
            context.pop();

            if (failed) {
                throw new RuntimeException("convertToArray() failed");
            }

            return array;
        }

        static Object[] convertToArray(Collection<?> source, RecordComponent component, Context context) {
            ParameterizedType targetListType = (ParameterizedType) component.getGenericType();
            Type[] parameterTypes = targetListType.getActualTypeArguments();
            Type parameterType = parameterTypes[0];
            if (!(parameterType instanceof Class<?> elementType)) {
                throw new IllegalStateException();
            }
            return convertToArray(source, elementType, context);
        }

        static Map<Class<?>, TypeConverter> buildConverterMap() {
            var map = new HashMap<Class<?>, TypeConverter>();
            for (var value : values()) {
                map.put(value.targetClass, value);
            }
            return Collections.unmodifiableMap(map);
        }

        static TypeConverter getConverter(Class<?> targetClass, Context context) {
            var resolvedClass = context.resolveType(targetClass);
            if (resolvedClass.isPrimitive()) {
                var name = targetClass.getName();
                return switch (name) {
                    case "boolean" -> BOOLEAN;
                    case "short" -> SHORT;
                    case "int" -> INTEGER;
                    case "long" -> LONG;
                    default -> throw new IllegalArgumentException(name);
                };
            } else if (resolvedClass.isEnum()) {
                return ENUM;
            } else if (resolvedClass.isRecord()) {
                return RECORD;
            } else if (CONVERTER_MAP.containsKey(resolvedClass)) {
                return CONVERTER_MAP.get(resolvedClass);
            }
            throw new ConversionException(bundle -> bundle.UNSUPPORTED_TARGET_TYPE(targetClass));
        }
    }


    static <T> T requireType(Object value, Class<T> targetClass) {
        if (!targetClass.isInstance(value)) {
            throw new ConversionException(bundle -> bundle.TYPE_MISMATCH(targetClass, value.getClass()));
        }
        return targetClass.cast(value);
    }

    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> Class<T> requireEnum(Class<?> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException();
        }
        return (Class<T>) clazz;
    }

    @SuppressWarnings("unchecked")
    static Class<? extends Record> requireRecord(Class<?> clazz) {
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException();
        }
        return (Class<? extends Record>) clazz;
    }

    static void handleException(Exception e, Context context) {
        if (e instanceof ConversionException ce) {
            Message m = ce.getMessage(context.messageBundle());
            context.addProblem(System.Logger.Level.ERROR, m);
        }
    }

    static class ConversionException extends RuntimeException {

        private final Function<MessageBundle, Message> messageFunction;

        ConversionException(Function<MessageBundle, Message> messageFunction) {
            this.messageFunction = messageFunction;
        }

        Message getMessage(MessageBundle bundle) {
            return messageFunction.apply(bundle);
        }
    }
}
