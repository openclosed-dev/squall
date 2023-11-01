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

package dev.openclosed.squall.api.sql.annotation;

import java.util.Objects;

/**
 * "label" annotation.
 * @param value the value of the annotation.
 */
public record Label(String value) implements DocAnnotation<String> {

    /**
     * Creates an instance of a {@code Label} record class.
     * @param value the value of the annotation.
     * @throws IllegalArgumentException if the specified value is blank.
     */
    public Label {
        Objects.requireNonNull(value);
        if (value.isBlank()) {
            throw new IllegalArgumentException("value must not be blank.");
        }
    }

    @Override
    public String name() {
        return "label";
    }
}
