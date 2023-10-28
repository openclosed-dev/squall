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
 * Visitor of components in the database specification.
 */
public interface SpecVisitor {

    /**
     * Visits a database.
     * @param database the database to visit.
     */
    default void visit(Database database) {
        visitChildren(database);
    }

    /**
     * Visits a schema.
     * @param schema the schema to visit.
     */
    default void visit(Schema schema) {
        visitChildren(schema);
    }

    /**
     * Visits a sequence.
     * @param sequence the sequence to visit.
     */
    default void visit(Sequence sequence) {
    }

    /**
     * Visits a table.
     * @param table the table to visit.
     */
    default void visit(Table table) {
        visitChildren(table);
    }

    /**
     * Visits a column in a table.
     * @param column the column to visit.
     */
    default void visit(Column column) {
    }

    /**
     * Visits the children of ths specified component, in the default order.
     * @param component the parent component containing children to visit.
     */
    default void visitChildren(Component component) {
        visitChildren(component, ComponentOrder.NAME);
    }

    /**
     * Visits the children of ths specified component.
     * @param component the parent component containing children to visit.
     * @param order the order in which the children will be visited.
     */
    default void visitChildren(Component component, ComponentOrder order) {
        Objects.requireNonNull(component);
        Objects.requireNonNull(order);
        component.children(order).forEach(child -> child.accept(this));
    }
}
