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

package dev.openclosed.squall.parser.basic;

import dev.openclosed.squall.api.sql.expression.ObjectRef;

public class NameResolver {

    private final String defaultSchema;
    private String currentDatabase;

    public NameResolver(String defaultSchema) {
        this.defaultSchema = defaultSchema;
        this.currentDatabase = "";
    }

    public void setCurrentDatabase(String currentDatabase) {
        this.currentDatabase = currentDatabase;
    }

    public ObjectRef resolveDotNotation(String name) {
        String[] names = name.split("\\.");
        if (names.length >= 3) {
            return new ObjectRef(names[0], names[1], names[2]);
        } else if (names.length == 2) {
            return resolveName(names[0], names[1]);
        } else if (names.length == 1) {
            return resolveName(names[0]);
        } else {
            throw new IllegalArgumentException("names");
        }
    }

    public ObjectRef resolveName(String name) {
        return resolveName(
            name,
            this.currentDatabase,
            this.defaultSchema
        );
    }

    public ObjectRef resolveName(String qualifier, String name) {
        return resolveName(name, this.currentDatabase, qualifier);
    }

    public ObjectRef resolveName(String name, String database, String schema) {
        return new ObjectRef(database, schema, name);
    }
}
