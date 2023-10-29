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

import dev.openclosed.squall.api.sql.annotation.DocAnnotation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Table in a database schema.
 * @param name the name of this table.
 * @param parents the parents of this table.
 * @param columns the list of the columns in this table.
 * @param primaryKey the primary key.
 * @param foreignKeys the list of foreign keys.
 * @param unique the list of unique constraints on this table.
 * @param annotations the annotations attached to the table.
 */
public record Table(
    String name,
    List<String> parents,
    List<Column> columns,
    Optional<PrimaryKey> primaryKey,
    List<ForeignKey> foreignKeys,
    List<Unique> unique,
    List<DocAnnotation> annotations
    ) implements SchemaObject {

    /**
     * Creates an instance of a {@code Table} record class.
     * @param name the name of this table.
     * @param parents the parents of this table.
     * @param columns the list of the columns in this table.
     * @param primaryKey the primary key.
     * @param foreignKeys the list of foreign keys.
     * @param unique the list of unique constraints on this table.
     * @param annotations the annotations attached to the table.
     */
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

    /**
     * Returns the foreign keys containing the specified column.
     * @param column the name of the column.
     * @return filtered foreign keys as a stream.
     */
    public Stream<ForeignKey> foreignKeysContaining(String column) {
        Objects.requireNonNull(column);
        return foreignKeys().stream().filter(fk -> fk.containsKey(column));
    }

    /**
     * Returns whether this table contains any columns or not.
     * @return {@code true} if this table contains any columns, {@code false} if there exists no column.
     */
    public boolean hasColumns() {
        return !columns.isEmpty();
    }

    @Override
    public Stream<? extends Component> children(ComponentOrder order) {
        // always definition order
        return columns().stream();
    }
}
