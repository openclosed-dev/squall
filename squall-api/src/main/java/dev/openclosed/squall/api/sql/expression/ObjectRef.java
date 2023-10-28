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

package dev.openclosed.squall.api.sql.expression;

import java.util.List;
import java.util.Objects;

/**
 * Reference to a schema object.
 * @param databaseName the name of the database, may be empty.
 * @param schemaName the name of the schema, may be empty.
 * @param objectName the name of the schema object, cannot be empty.
 */
public record ObjectRef(String databaseName, String schemaName, String objectName) {

    /**
     * Creates an instance of a {@code ObjectRef} record class.
     * @param databaseName the name of the database, may be empty.
     * @param schemaName the name of the schema, may be empty.
     * @param objectName the name of the schema object, cannot be empty.
     */
    public ObjectRef {
        Objects.requireNonNull(databaseName);
        Objects.requireNonNull(schemaName);
        Objects.requireNonNull(objectName);
        if (objectName.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the name components as a list.
     * @return a list of the name components.
     */
    public List<String> toList() {
        return List.of(databaseName, schemaName, objectName);
    }
}
