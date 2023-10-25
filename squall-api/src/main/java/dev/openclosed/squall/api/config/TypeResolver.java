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

import java.util.Map;
import java.util.HashMap;

final class TypeResolver {

    private final Map<Class<?>, Class<?>> mapping;

    static Builder builder() {
        return new Builder();
    }

    private TypeResolver(Builder builder) {
        this.mapping = builder.mapping;
    }

    Class<?> resolveType(Class<?> type) {
        if (mapping.containsKey(type)) {
            return mapping.get(type);
        }
        return type;
    }

    static class Builder {
        private final Map<Class<?>, Class<?>> mapping = new HashMap<>();

        <T> Builder addMapping(Class<T> superType, Class<? extends T> subType) {
            mapping.put(superType, subType);
            return this;
        }

        TypeResolver build() {
            return new TypeResolver(this);
        }
    }
}
