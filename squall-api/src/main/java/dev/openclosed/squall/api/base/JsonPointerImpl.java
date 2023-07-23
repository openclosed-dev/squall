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

package dev.openclosed.squall.api.base;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An internal implementation of {@link JsonPointer}.
 */
final class JsonPointerImpl implements JsonPointer {

    public static final JsonPointer ROOT = new JsonPointerImpl(Collections.emptyList());

    public static JsonPointer of(List<String> tokens) {
        Objects.requireNonNull(tokens);
        if (tokens.isEmpty()) {
            return ROOT;
        } else {
            return new JsonPointerImpl(List.copyOf(tokens));
        }
    }

    private final List<String> tokens;

    private JsonPointerImpl(List<String> tokens) {
        this.tokens = tokens;
    }

    @Override
    public List<String> tokens() {
        return tokens;
    }

    @Override
    public String toString() {
        if (tokens.isEmpty()) {
            return "";
        }
        var b = new StringBuilder();
        for (var token : tokens) {
            b.append('/').append(escapeToken(token));
        }
        return b.toString();
    }

    private static String escapeToken(String token) {
        return token.replaceAll("~", "~0").replaceAll("/", "~1");
    }
}
