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

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record RecordType<T extends Record>(
        Class<T> type,
        Constructor<T> constructor,
        Map<String, Object> defaulValueMap) {

    public static <T extends Record> RecordType<T> of(Class<T> type) {
        try {
            return new RecordType<T>(
                    type,
                    getDefaultConstructor(type),
                    buildDefaultValueMap(type)
                    );
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public T newInstance(Object... args) throws ReflectiveOperationException {
        return constructor().newInstance(args);
    }

    public RecordComponent[] components() {
        return type().getRecordComponents();
    }

    private static <T extends Record> Constructor<T> getDefaultConstructor(Class<T> type)
            throws NoSuchMethodException {
        var components = type.getRecordComponents();
        Class<?>[] types = new Class<?>[components.length];
        int i = 0;
        for (var component : components) {
            types[i++] = component.getType();
        }
        return type.getConstructor(types);
    }

    private static <T extends Record> Map<String, Object> buildDefaultValueMap(Class<T> type)
            throws ReflectiveOperationException {
        Constructor<T> noArgsConstructor;
        try {
            noArgsConstructor = type.getConstructor();
        } catch (NoSuchMethodException e) {
            return Collections.emptyMap();
        }

        T object = noArgsConstructor.newInstance();
        return toShallowMap(object);
    }

    private static Map<String, Object> toShallowMap(Record rec) {
        Objects.requireNonNull(rec);

        var map = new LinkedHashMap<String, Object>();

        try {
            for (var component : rec.getClass().getRecordComponents()) {
                var name = component.getName();
                var value = component.getAccessor().invoke(rec);
                map.put(name, value);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        return map;
    }
}
