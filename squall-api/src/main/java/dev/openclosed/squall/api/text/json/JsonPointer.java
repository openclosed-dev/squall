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

package dev.openclosed.squall.api.text.json;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * JSON pointer.
 * @param tokens the reference tokens of this JSON pointer.
 */
public record JsonPointer(List<String> tokens) {

    private static final JsonPointer ROOT = new JsonPointer(Collections.emptyList());

    /**
     * Creates an instance of {@link JsonPointer} from reference tokens.
     * @param tokens the reference tokens composing the pointer.
     * @return created instance of JSON pointer.
     */
    public static JsonPointer of(List<String> tokens) {
        Objects.requireNonNull(tokens);
        if (tokens.isEmpty()) {
            return ROOT;
        } else {
            return new JsonPointer(tokens);
        }
    }

    /**
     * Constructs a JSON pointer.
     * @param tokens the reference tokens of this JSON pointer.
     */
    public JsonPointer {
        Objects.requireNonNull(tokens);
        tokens = List.copyOf(tokens);
    }

    /**
     * Return the string representation of this JSON pointer.
     * @return the string representation of this JSON pointer.
     */
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
