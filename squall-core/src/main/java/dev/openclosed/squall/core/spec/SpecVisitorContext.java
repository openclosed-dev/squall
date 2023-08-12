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

package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;

import java.util.ArrayList;
import java.util.List;

final class SpecVisitorContext implements SpecVisitor.Context {

    private final List<Component> componentStack;

    SpecVisitorContext() {
        this.componentStack = new ArrayList<>(8);
    }

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

    void pushComponent(Component component) {
        this.componentStack.add(component);
    }

    Component popComponent() {
        return this.componentStack.remove(this.componentStack.size() - 1);
    }
}
