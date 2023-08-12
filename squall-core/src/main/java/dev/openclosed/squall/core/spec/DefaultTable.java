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

import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.PrimaryKey;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;
import dev.openclosed.squall.api.spec.Unique;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record DefaultTable(
    String name,
    String qualifiedName,
    List<Column> columns,
    Optional<PrimaryKey> primaryKey,
    List<ForeignKey> foreignKeys,
    List<Unique> unique,
    List<DocAnnotation> annotations
    ) implements Table, BasicComponent {

    public DefaultTable {
        Objects.requireNonNull(name);
        Objects.requireNonNull(qualifiedName);
        columns = List.copyOf(columns);
        foreignKeys = List.copyOf(foreignKeys);
        unique = List.copyOf(unique);
    }

    @Override
    public void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, SpecVisitorContext context) {
        visitor.visit(this, ordinal, context);
        // Forces to visit in definition order
        visitChildren(columns, visitor, ComponentOrder.DEFINITION, context);
        visitor.leave(this);
    }

    @Override
    public boolean hasColumns() {
        return !columns.isEmpty();
    }
}
