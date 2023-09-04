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

package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class SpecWalker implements SpecVisitor.Context {

    private final SpecVisitor visitor;
    private final ComponentOrder order;
    private final List<Component> componentStack;

    SpecWalker(SpecVisitor visitor, ComponentOrder order) {
        this.visitor = visitor;
        this.order = order;
        this.componentStack = new ArrayList<>(8);
    }

    void walkOnSpec(DatabaseSpec spec) {
        this.visitor.visit(spec, this);
        walkOnDatabases(spec.databases());
        this.visitor.leave(spec);
    }

    // SpecVisitor.Context

    @Override
    public Database currentDatabase() {
        if (componentStack.size() < 1) {
            throw new IllegalStateException("No database");
        }
        return (Database) componentStack.get(0);
    }

    @Override
    public Schema currentSchema() {
        if (componentStack.size() < 2) {
            throw new IllegalStateException("No schema");
        }
        return (Schema) componentStack.get(1);
    }

    @Override
    public Table currentTable() {
        if (componentStack.size() < 3) {
            throw new IllegalStateException("No table");
        }
        return (Table) componentStack.get(2);
    }

    //

    void pushComponent(Component component) {
        this.componentStack.add(component);
    }

    Component popComponent() {
        return this.componentStack.remove(this.componentStack.size() - 1);
    }

    private void walkOnDatabases(List<Database> databases) {
        int ordinal = 1;
        for (var child : reorder(databases)) {
            walkOnDatabase(child, ordinal++);
        }
    }

    private void walkOnDatabase(Database database, int ordinal) {
        this.visitor.visit(database, ordinal, this);
        pushComponent(database);
        walkOnSchemas(database.schemas());
        popComponent();
        this.visitor.leave(database);
    }

    private void walkOnSchemas(List<Schema> schemas) {
        int ordinal = 1;
        for (var child : reorder(schemas)) {
            walkOnSchema(child, ordinal++);
        }
    }

    private void walkOnSchema(Schema schema, int ordinal) {
        this.visitor.visit(schema, ordinal, this);
        pushComponent(schema);
        walkOnSequences(schema.sequences());
        walkOnTables(schema.tables());
        popComponent();
        this.visitor.leave(schema);
    }

    private void walkOnSequences(List<Sequence> sequences) {
        int ordinal = 1;
        for (var child : reorder(sequences)) {
            walkOnSequence(child, ordinal++);
        }
    }

    private void walkOnSequence(Sequence sequence, int ordinal) {
        this.visitor.visit(sequence, ordinal, this);
    }

    private void walkOnTables(List<Table> tables) {
        int ordinal = 1;
        for (var child : reorder(tables)) {
            walkOnTable(child, ordinal++);
        }
    }

    private void walkOnTable(Table table, int ordinal) {
        this.visitor.visit(table, ordinal, this);
        pushComponent(table);
        walkOnColumns(table.columns());
        popComponent();
        this.visitor.leave(table);
    }

    private void walkOnColumns(List<Column> columns) {
        int ordinal = 1;
        for (var child : columns) {
            walkOnColumn(child, ordinal++);
        }
    }

    private void walkOnColumn(Column column, int ordinal) {
        this.visitor.visit(column, ordinal, this);
    }

    private <T extends Component> Collection<T> reorder(Collection<T> components) {
        return this.order.reorder(components);
    }
}
