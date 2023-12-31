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

/**
 * SQL Dialect.
 */
public interface Dialect {

    /**
     * Returns the name of this dialect.
     * @return the name of this dialect.
     */
    String name();

    /**
     * Returns the default schema in this dialect.
     * @return the default schema in this dialect.
     */
    String defaultSchema();

    /**
     * PostgreSQL.
     */
    Dialect POSTGRESQL = new SimpleDialect("postgresql", "public");
}

record SimpleDialect(String name, String defaultSchema) implements Dialect {
}
