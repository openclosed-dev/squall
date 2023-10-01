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

import java.util.Objects;

/**
 * A visitor of components in the database specification.
 */
public interface SpecVisitor {

    /**
     * Visits a database.
     * @param database the visited database.
     */
    default void visit(Database database) {
        visitChildren(database);
    }

    /**
     * Visits a schema.
     * @param schema the visited schema.
     */
    default void visit(Schema schema) {
        visitChildren(schema);
    }

    /**
     * Visits a sequence.
     * @param sequence the visited sequence.
     */
    default void visit(Sequence sequence) {
    }

    /**
     * Visits a table.
     * @param table the visited table.
     */
    default void visit(Table table) {
        visitChildren(table);
    }

    /**
     * Visits a column in a table.
     * @param column the visited column.
     */
    default void visit(Column column) {
    }

    default void visitChildren(Component component) {
        visitChildren(component, ComponentOrder.NAME);
    }

    default void visitChildren(Component component, ComponentOrder order) {
        Objects.requireNonNull(component);
        Objects.requireNonNull(order);
        component.children(order).forEach(child -> child.accept(this));
    }
}
