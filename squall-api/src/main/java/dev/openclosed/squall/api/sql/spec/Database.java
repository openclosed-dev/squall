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

public record Database(
    String name,
    List<Schema> schemas,
    List<DocAnnotation> annotations,
    Component.State state
    ) implements Component {

    public Database {
        Objects.requireNonNull(name);
        Objects.requireNonNull(schemas);
        Objects.requireNonNull(annotations);
        Objects.requireNonNull(state);
        schemas = List.copyOf(schemas);
        annotations = List.copyOf(annotations);
    }

    @Override
    public Type type() {
        return Type.DATABASE;
    }

    @Override
    public void accept(SpecVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Stream<? extends Component> children(ComponentOrder order) {
        return order.reorder(schemas().stream());
    }
}
