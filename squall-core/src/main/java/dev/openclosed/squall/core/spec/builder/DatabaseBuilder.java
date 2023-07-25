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

package dev.openclosed.squall.core.spec.builder;

import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.core.spec.DefaultDatabase;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class DatabaseBuilder extends ComponentBuilder {

    private final Component.State state;
    private final Map<String, SchemaBuilder> schemaBuilders = new LinkedHashMap<>();

    DatabaseBuilder(String name, List<DocAnnotation> annotations, Component.State state) {
        super(name, annotations);
        this.state = state;
    }

    Database build() {
        var schemas = this.schemaBuilders.values().stream()
            .map(SchemaBuilder::build).toList();
        return new DefaultDatabase(name(), schemas, annotations(), state);
    }

    void addSchema(String name, List<DocAnnotation> annotations) {
        addAnnotatedSchema(name, annotations, Component.State.DEFINED);
    }

    SchemaBuilder getSchema(String name) {
        var builder = schemaBuilders.get(name);
        if (builder == null) {
            builder = addAnnotatedSchema(name, Collections.emptyList(), Component.State.UNDEFINED);
        }
        return builder;
    }

    private SchemaBuilder addAnnotatedSchema(String name, List<DocAnnotation> annotations, Component.State state) {
        var builder = new SchemaBuilder(name, annotations, state);
        schemaBuilders.put(name, builder);
        return builder;
    }
}
