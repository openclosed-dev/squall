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

package dev.openclosed.squall.core.sql.spec.builder;

import dev.openclosed.squall.api.sql.expression.ObjectRef;
import dev.openclosed.squall.api.sql.spec.Column;
import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.spec.DocAnnotation;
import dev.openclosed.squall.api.sql.spec.ForeignKey;
import dev.openclosed.squall.api.sql.spec.PrimaryKey;
import dev.openclosed.squall.api.sql.spec.Table;
import dev.openclosed.squall.api.sql.spec.Unique;
import dev.openclosed.squall.core.sql.spec.DefaultForeignKey;
import dev.openclosed.squall.core.sql.spec.DefaultPrimaryKey;
import dev.openclosed.squall.core.sql.spec.DefaultTable;
import dev.openclosed.squall.core.sql.spec.DefaultUnique;

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

    TableBuilder(String name, List<String> parents, List<DocAnnotation> annotations) {
        super(name, parents, annotations);
    }

    Table build() {
        return new DefaultTable(
            name(),
            parents(),
            buildColumns(),
            Optional.ofNullable(primaryKey),
            foreignKeys,
            unique,
            annotations());
    }

    ColumnBuilder addColumn(String name,
                            DataType dataType,
                            List<DocAnnotation> annotations) {
        var builder = new ColumnBuilder(name, parentsForChild(), dataType, annotations);
        this.columns.put(name, builder);
        return builder;
    }

    void setPrimaryKey(String name, List<String> columnNames) {
        this.primaryKey = new DefaultPrimaryKey(Optional.ofNullable(name), columnNames);
    }

    void addUnique(String name, List<String> columns) {
        unique.add(new DefaultUnique(Optional.ofNullable(name), columns));
    }

    void addForeignKey(String name, ObjectRef tableRef, List<String> columns, List<String> refColumns) {
        var columnMapping = new LinkedHashMap<String, String>();
        for (int i = 0; i < columns.size(); i++) {
            columnMapping.put(columns.get(i), refColumns.get(i));
        }

        foreignKeys.add(new DefaultForeignKey(
            Optional.ofNullable(name),
            tableRef.toList(),
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
                builder.setPrimaryKey(true);
                builder.setRequired(true);
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
