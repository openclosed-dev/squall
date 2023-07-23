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

package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.spec.Component;

enum Badge {
    DATABASE("indigo"),
    SCHEMA("green"),
    TABLE("steelblue");

    private final String color;

    private static final String BASE_URL = "https://img.shields.io/badge/";

    Badge(String color) {
        this.color = color;
    }

    final String label() {
        return name().toLowerCase();
    }

    final String color() {
        return color;
    }

    final String url() {
        return new StringBuilder()
            .append(BASE_URL)
            .append(label())
            .append('-')
            .append(color())
            .toString();
    }

    static Badge mapComponentType(Component.Type componentType) {
        return switch (componentType) {
            case DATABASE ->  DATABASE;
            case SCHEMA -> SCHEMA;
            case TABLE -> TABLE;
            default -> throw new IllegalArgumentException();
        };
    }
}
