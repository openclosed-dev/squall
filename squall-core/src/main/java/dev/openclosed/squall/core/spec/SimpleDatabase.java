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
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.core.base.RecordMapSource;

import java.util.List;

public record SimpleDatabase(
        String name,
        List<Schema> schemas,
        List<DocAnnotation> annotations,
        Component.State state
        ) implements Database, RecordMapSource {

    @Override
    public Type type() {
        return Type.DATABASE;
    }

    @Override
    public void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal) {
        visitor.visit(this, ordinal);
        Components.visitOrderedComponents(this.schemas, visitor, order);
        visitor.leave(this);
    }
}
