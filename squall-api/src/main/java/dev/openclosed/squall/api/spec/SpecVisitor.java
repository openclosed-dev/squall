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

package dev.openclosed.squall.api.spec;

/**
 * A visitor of components in the database specification.
 */
public interface SpecVisitor {

    /**
     * Visits the database specification.
     * @param spec the visited database specification.
     */
    default void visit(DatabaseSpec spec) {
    }

    /**
     * Leaves the database specification.
     * @param spec the visited database specification.
     */
    default void leave(DatabaseSpec spec) {
    }

    /**
     * Visits a database.
     * @param database the visited database.
     * @param ordinal the ordinal number of the visited component.
     */
    default void visit(Database database, int ordinal) {
    }

    /**
     * Leaves the database.
     * @param database the visited database.
     */
    default void leave(Database database) {
    }

    /**
     * Visits a schema.
     * @param schema the visited schema.
     * @param ordinal the ordinal number of the visited component.
     */
    default void visit(Schema schema, int ordinal) {
    }

    /**
     * Leaves the schema.
     * @param schema the visited schema.
     */
    default void leave(Schema schema) {
    }

    /**
     * Visits a sequence.
     * @param sequence the visited sequence.
     * @param ordinal the ordinal number of the visited component.
     * @param schema the parent schema.
     */
    default void visit(Sequence sequence, int ordinal, Schema schema) {
    }

    /**
     * Leaves the sequence.
     * @param sequence the visited sequence.
     */
    default void leave(Sequence sequence) {
    }

    /**
     * Visits a table.
     * @param table the visited table.
     * @param ordinal the ordinal number of the visited component.
     * @param schema the parent schema.
     */
    default void visit(Table table, int ordinal, Schema schema) {
    }

    /**
     * Leaves the table.
     * @param table the visited table.
     */
    default void leave(Table table) {
    }

    /**
     * Visits a column in a table.
     * @param column the visited column.
     * @param ordinal the ordinal number of the visited component.
     * @param table the table owning the column.
     */
    default void visit(Column column, int ordinal, Table table) {
    }
}
