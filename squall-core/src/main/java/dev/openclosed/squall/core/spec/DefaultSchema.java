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
import dev.openclosed.squall.api.spec.Table;
import dev.openclosed.squall.core.base.RecordMapSource;

import java.util.List;

public record DefaultSchema(
        String name,
        List<Sequence> sequences,
        List<Table> tables,
        List<DocAnnotation> annotations,
        Component.State state
        ) implements Schema, RecordMapSource {

    @Override
    public Type type() {
        return Type.SCHEMA;
    }

    @Override
    public void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, Component parent) {
        visitor.visit(this, ordinal);
        Components.visitChildComponents(this.sequences, visitor, order, this);
        Components.visitChildComponents(this.tables, visitor, order, this);
        visitor.leave(this);
    }
}
