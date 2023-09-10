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
import java.util.function.Predicate;

final class SpecWalker implements SpecVisitor.Context {

    private final ComponentOrder order;
    private final Predicate<Component> filter;
    private final SpecVisitor visitor;

    private final List<Component> componentStack;

    SpecWalker(ComponentOrder order, Predicate<Component> filter, SpecVisitor visitor) {
        this.order = order;
        this.filter = filter;
        this.visitor = visitor;

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

    boolean shouldVisit(Component component) {
        return filter.test(component);
    }

    private void walkOnDatabases(List<Database> databases) {
        for (var child : reorder(databases)) {
            walkOnDatabase(child);
        }
    }

    private void walkOnDatabase(Database database) {
        final boolean shouldVisit = shouldVisit(database);
        if (shouldVisit) {
            this.visitor.visit(database, this);
        }
        pushComponent(database);
        walkOnSchemas(database.schemas());
        popComponent();
        if (shouldVisit) {
            this.visitor.leave(database);
        }
    }

    private void walkOnSchemas(List<Schema> schemas) {
        for (var child : reorder(schemas)) {
            walkOnSchema(child);
        }
    }

    private void walkOnSchema(Schema schema) {
        final boolean shouldVisit = shouldVisit(schema);
        if (shouldVisit) {
            this.visitor.visit(schema, this);
        }
        pushComponent(schema);
        walkOnSequences(schema.sequences());
        walkOnTables(schema.tables());
        popComponent();
        if (shouldVisit) {
            this.visitor.leave(schema);
        }
    }

    private void walkOnSequences(List<Sequence> sequences) {
        for (var child : reorder(sequences)) {
            walkOnSequence(child);
        }
    }

    private void walkOnSequence(Sequence sequence) {
        if (shouldVisit(sequence)) {
            this.visitor.visit(sequence, this);
        }
    }

    private void walkOnTables(List<Table> tables) {
        for (var child : reorder(tables)) {
            walkOnTable(child);
        }
    }

    private void walkOnTable(Table table) {
        final boolean shouldVisit = shouldVisit(table);
        if (shouldVisit) {
            this.visitor.visit(table, this);
        }
        pushComponent(table);
        walkOnColumns(table.columns());
        popComponent();
        if (shouldVisit) {
            this.visitor.leave(table);
        }
    }

    private void walkOnColumns(List<Column> columns) {
        for (var child : columns) {
            walkOnColumn(child);
        }
    }

    private void walkOnColumn(Column column) {
        if (shouldVisit(column)) {
            this.visitor.visit(column, this);
        }
    }

    private <T extends Component> Collection<T> reorder(Collection<T> components) {
        return this.order.reorder(components);
    }
}
