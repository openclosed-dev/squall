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

import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.core.base.RecordMapSource;

import java.util.List;
import java.util.Optional;

public record DefaultDatabaseSpec(
        Optional<String> title,
        List<Database> databases
        ) implements DatabaseSpec, RecordMapSource {

    public void walkSpec(SpecVisitor visitor, ComponentOrder order) {
        visitor.visit(this);
        Components.visitOrderedComponents(this.databases, visitor, order);
        visitor.leave(this);
    }
}