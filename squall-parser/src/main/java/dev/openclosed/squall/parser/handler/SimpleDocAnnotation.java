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

package dev.openclosed.squall.parser.handler;

import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.DocAnnotationType;

import java.util.Map;

/**
 * Simple implementation of {@link DocAnnotation}.
 * @param type the type of the annotation.
 * @param value the value given for this annotation.
 */
record SimpleDocAnnotation(DocAnnotationType type, String value) implements DocAnnotation {

    @Override
    public Map<String, Object> toMap() {
        return Map.of(
            "type", type().name().toLowerCase(),
            "value", value()
        );
    }
}