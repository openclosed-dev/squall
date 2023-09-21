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
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.Table;

import java.util.List;
import java.util.stream.Stream;

public record DefaultSchema(
        String name,
        List<String> parents,
        List<Sequence> sequences,
        List<Table> tables,
        List<DocAnnotation> annotations,
        Component.State state
        ) implements Schema, BasicComponent {

    public DefaultSchema {
        parents = List.copyOf(parents);
        sequences = List.copyOf(sequences);
        tables = List.copyOf(tables);
        annotations = List.copyOf(annotations);
    }

    @Override
    public Type type() {
        return Type.SCHEMA;
    }

    @Override
    public Stream<? extends Component> children(ComponentOrder order) {
        return Stream.concat(
            order.reorder(sequences().stream()),
            order.reorder(tables.stream())
        );
    }
}
