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

import dev.openclosed.squall.api.sql.spec.Component;

import java.io.InputStream;
import java.util.NoSuchElementException;

enum Badge {
    DATABASE,
    SCHEMA,
    TABLE,
    SEQUENCE;

    private static final String BASE_URL = "./images/";

    final String label() {
        return name().toLowerCase();
    }

    final String url() {
        return new StringBuilder()
            .append(BASE_URL)
            .append(filename())
            .toString();
    }

    final String filename() {
        return label() + ".svg";
    }

    final InputStream getResourceAsStream() {
        return getClass().getResourceAsStream(filename());
    }

    static Badge mapComponentType(Component.Type componentType) {
        return switch (componentType) {
            case DATABASE ->  DATABASE;
            case SCHEMA -> SCHEMA;
            case TABLE -> TABLE;
            case SEQUENCE -> SEQUENCE;
            case COLUMN -> throw new NoSuchElementException();
        };
    }
}
