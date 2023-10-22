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
 * A foreign key constraint.
 * @param constraintName the name of the constraint.
 * @param tableName the name of the table including parents.
 * @param columnMapping the mapping of the columns.
 */
public record ForeignKey(
    Optional<String> constraintName,
    List<String> tableName,
    Map<String, String> columnMapping
    ) implements Constraint {

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
     * Returns the referenced table name.
     * @return the referenced table name.
     */
    public String simpleTableName() {
        var name = tableName();
        return name.get(name.size() - 1);
    }

    /**
     * Returns the referenced table name in fully qualified form.
     * @return the referenced table name in fully qualified form.
     */
    public String fullTableName() {
        return tableName().stream().collect(Collectors.joining("."));
    }

    public boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }
}
