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
     * Visiting context.
     */
    interface Context {

        Database currentDatabase();

        Schema currentSchema();

        Table currentTable();
    }

    /**
     * Visits the database specification.
     * @param spec the visited database specification.
     * @param context the context of this visiting.
     */
    default void visit(DatabaseSpec spec, Context context) {
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
     * @param context the context of this visiting.
     */
    default void visit(Database database, Context context) {
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
     * @param context the context of this visiting.
     */
    default void visit(Schema schema, Context context) {
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
     * @param context the context of this visiting.
     */
    default void visit(Sequence sequence, Context context) {
    }

    /**
     * Visits a table.
     * @param table the visited table.
     * @param context the context of this visiting.
     */
    default void visit(Table table, Context context) {
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
     * @param context the context of this visiting.
     */
    default void visit(Column column, Context context) {
    }
}
