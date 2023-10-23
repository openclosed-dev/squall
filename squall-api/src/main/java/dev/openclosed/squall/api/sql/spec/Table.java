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

package dev.openclosed.squall.api.sql.spec;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public record Table(
    String name,
    List<String> parents,
    List<Column> columns,
    Optional<PrimaryKey> primaryKey,
    List<ForeignKey> foreignKeys,
    List<Unique> unique,
    List<DocAnnotation> annotations
    ) implements SchemaObject {

    public Table {
        Objects.requireNonNull(name);
        parents = List.copyOf(parents);
        columns = List.copyOf(columns);
        foreignKeys = List.copyOf(foreignKeys);
        unique = List.copyOf(unique);
    }

    @Override
    public Type type() {
        return Type.TABLE;
    }

    @Override
    public void accept(SpecVisitor visitor) {
        visitor.visit(this);
    }

    public Stream<ForeignKey> foreignKeysContaining(String column) {
        return foreignKeys().stream().filter(fk -> fk.containsKey(column));
    }

    public boolean hasColumns() {
        return !columns.isEmpty();
    }

    @Override
    public Stream<? extends Component> children(ComponentOrder order) {
        // always definition order
        return columns().stream();
    }
}
