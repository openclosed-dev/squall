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

import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;

import java.util.List;
import java.util.Objects;

public record DefaultSequence(
    String name,
    String qualifiedName,
    String typeName,
    long start,
    long increment,
    long maxValue,
    long minValue,
    List<DocAnnotation> annotations) implements Sequence, BasicComponent {

    public DefaultSequence {
        Objects.requireNonNull(name);
        Objects.requireNonNull(qualifiedName);
        Objects.requireNonNull(typeName);
    }

    @Override
    public Type type() {
        return Type.SEQUENCE;
    }

    @Override
    public void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, Component parent) {
        if (!(parent instanceof Schema schema)) {
            throw new IllegalArgumentException("Illegal parent");
        }
        visitor.visit(this, ordinal, schema);
        visitor.leave(this);
    }
}
