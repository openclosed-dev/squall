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
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.core.spec.DefaultSchema;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class SchemaBuilder extends ComponentBuilder {

    private final Component.State state;
    private final Map<String, TableBuilder> tableBuilders = new LinkedHashMap<>();
    private final Map<String, SequenceBuilder> sequenceBuilders = new LinkedHashMap<>();

    SchemaBuilder(String name, List<DocAnnotation> annotations, Component.State state) {
        super(name, annotations);
        this.state = state;
    }

    Schema build() {
        var sequences = this.sequenceBuilders.values().stream()
            .map(SequenceBuilder::build).toList();
        var tables = this.tableBuilders.values().stream()
            .map(TableBuilder::build).toList();
        return new DefaultSchema(name(), sequences, tables, annotations(), state);
    }

    TableBuilder addTable(String tableName, List<DocAnnotation> annotations) {
        var builder = new TableBuilder(tableName, getQualifiedName(tableName), annotations);
        tableBuilders.put(tableName, builder);
        return builder;
    }

    TableBuilder getTableToAlter(String name) {
        return tableBuilders.get(name);
    }

    SequenceBuilder addSequence(String sequenceNmae, List<DocAnnotation> annotations) {
        var builder = new SequenceBuilder(sequenceNmae, getQualifiedName(sequenceNmae), annotations);
        sequenceBuilders.put(sequenceNmae, builder);
        return builder;
    }

    private String getQualifiedName(String componentName) {
        String schemaName = name();
        if (schemaName.isEmpty()) {
            return componentName;
        }
        return new StringBuilder()
            .append(schemaName).append('.').append(componentName)
            .toString();
    }
}
