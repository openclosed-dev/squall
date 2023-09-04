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

package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Sequence;

import java.util.List;
import java.util.Objects;

public record DefaultSequence(
    String name,
    List<String> parents,
    String typeName,
    long start,
    long increment,
    long maxValue,
    long minValue,
    List<DocAnnotation> annotations) implements Sequence, SchemaObject {

    public DefaultSequence {
        Objects.requireNonNull(name);
        Objects.requireNonNull(typeName);
        parents = List.copyOf(parents);
    }

    @Override
    public Type type() {
        return Type.SEQUENCE;
    }
}
