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

import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.ForeignKey;
import dev.openclosed.squall.api.spec.PrimaryKey;
import dev.openclosed.squall.api.spec.Table;
import dev.openclosed.squall.api.spec.TableRef;
import dev.openclosed.squall.api.spec.Unique;
import dev.openclosed.squall.core.spec.DefaultForeignKey;
import dev.openclosed.squall.core.spec.DefaultPrimaryKey;
import dev.openclosed.squall.core.spec.DefaultTable;
import dev.openclosed.squall.core.spec.DefaultUnique;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

final class TableBuilder extends ComponentBuilder {

    private final Map<String, ColumnBuilder> columns = new LinkedHashMap<>();
    private PrimaryKey primaryKey;
    private final List<Unique> unique = new ArrayList<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();

    TableBuilder(String name, List<DocAnnotation> annotations) {
        super(name, annotations);
    }

    Table build() {
        return new DefaultTable(
            name(),
            buildColumns(),
            Optional.ofNullable(primaryKey),
            foreignKeys,
            unique,
            annotations());
    }

    ColumnBuilder addColumn(String name,
                            DataType dataType,
                            List<DocAnnotation> annotations) {
        var builder = new ColumnBuilder(name, dataType, annotations);
        this.columns.put(name, builder);
        return builder;
    }

    void setPrimaryKey(String name, List<String> columnNames) {
        this.primaryKey = new DefaultPrimaryKey(Optional.ofNullable(name), columnNames);
    }

    void addUnique(String name, List<String> columns) {
        unique.add(new DefaultUnique(Optional.ofNullable(name), columns));
    }

    void addForeignKey(String name, TableRef table, Map<String, String> columnMapping) {
        foreignKeys.add(new DefaultForeignKey(
            Optional.ofNullable(name),
            table,
            columnMapping
        ));
    }

    private List<Column> buildColumns() {
        modifyPrimaryKeyColumns();
        modifyUniqueColumns();
        return this.columns.values().stream()
            .map(ColumnBuilder::build).toList();
    }

    private void modifyPrimaryKeyColumns() {
        if (primaryKey == null) {
            return;
        }
        for (var column : primaryKey.columns()) {
            var builder = this.columns.get(column);
            if (builder != null) {
                builder.setNullable(false);
            }
        }
        if (!primaryKey.isComposite()) {
            var builder = this.columns.get(primaryKey.columns().iterator().next());
            builder.setUnique(true);
        }
    }

    private void modifyUniqueColumns() {
        this.unique.stream().filter(u -> !u.isComposite())
            .forEach(u -> {
                var builder = this.columns.get(u.columns().get(0));
                builder.setUnique(true);
            });
    }
}
