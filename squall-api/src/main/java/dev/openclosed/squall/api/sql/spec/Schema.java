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

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public record Schema(
    String name,
    List<String> parents,
    List<Sequence> sequences,
    List<Table> tables,
    List<DocAnnotation> annotations,
    Component.State state
    ) implements Component {

    public Schema {
        Objects.requireNonNull(name);
        Objects.requireNonNull(parents);
        Objects.requireNonNull(sequences);
        Objects.requireNonNull(tables);
        Objects.requireNonNull(annotations);
        Objects.requireNonNull(state);
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
    public void accept(SpecVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Stream<? extends Component> children(ComponentOrder order) {
        return Stream.concat(
            order.reorder(sequences().stream()),
            order.reorder(tables.stream())
        );
    }
}
