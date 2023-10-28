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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Foreign key constraint.
 * @param constraintName the name of the constraint, optional.
 * @param tableName the name of the referenced table, including parents.
 * @param columnMapping the mapping of the columns.
 */
public record ForeignKey(
    Optional<String> constraintName,
    List<String> tableName,
    Map<String, String> columnMapping
    ) implements Constraint {

    /**
     * Creates an instance of a {@code ForeignKey} record class.
     * @param constraintName the name of the constraint, optional.
     * @param tableName the name of the referenced table, including parents.
     * @param columnMapping the mapping of the columns.
     */
    public ForeignKey {
        Objects.requireNonNull(tableName);
        Objects.requireNonNull(columnMapping);
        if (columnMapping.isEmpty()) {
            throw new IllegalArgumentException("columnMapping must not be empty");
        }
        tableName = List.copyOf(tableName);
        columnMapping = Map.copyOf(columnMapping);
    }

    /**
     * Returns the name of the referenced table.
     * @return the name of the referenced table.
     */
    public String simpleTableName() {
        var name = tableName();
        return name.get(name.size() - 1);
    }

    /**
     * Returns the fully qualified name of the referenced table.
     * @return the fully qualified name of the referenced table.
     */
    public String fullTableName() {
        return tableName().stream().collect(Collectors.joining("."));
    }

    /**
     * Returns whether this foreign key contains the specified column or not.
     * @param column the name of the column to test.
     * @return {@code true} if this foreign key contains the column, {@code false} otherwise.
     */
    public boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }
}
