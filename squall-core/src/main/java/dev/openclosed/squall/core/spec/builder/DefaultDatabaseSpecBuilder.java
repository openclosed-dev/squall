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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Component.State;
import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.SchemaObjectRef;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import dev.openclosed.squall.core.spec.DefaultDatabaseSpec;

/**
 * The default implementation of {@link DatabaseSpecBuilder}.
 */
public final class DefaultDatabaseSpecBuilder implements DatabaseSpecBuilder {

    private final Map<String, DatabaseBuilder> databaseBuilders = new LinkedHashMap<>();
    private DatabaseBuilder currentDatabaseBuilder;
    private TableBuilder currentTableBuilder;
    private ColumnBuilder currentColumnBuilder;
    private SequenceBuilder currentSequenceBuilder;
    private String title;

    @Override
    public DatabaseSpecBuilder setTitle(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }

    @Override
    public DatabaseSpecBuilder addDatabase(String name, List<DocAnnotation> annotations) {
        Objects.requireNonNull(name);
        addAnnotatedDatabase(name, annotations, State.DEFINED);
        changeCurrentDatabase(name);
        return this;
    }

    @Override
    public DatabaseSpecBuilder changeCurrentDatabase(String name) {
        Objects.requireNonNull(name);
        this.currentDatabaseBuilder = findDatabase(name);
        return this;
    }

    public DatabaseBuilder getCurrentDatabase() {
        if (currentDatabaseBuilder == null) {
            currentDatabaseBuilder = addUndefinedDatabase("");
        }
        return currentDatabaseBuilder;
    }

    @Override
    public DatabaseSpecBuilder addSchema(String name, List<DocAnnotation> annotations) {
        Objects.requireNonNull(name);
        getCurrentDatabase().addSchema(name, annotations);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addTable(String schemaName, String tableName, List<DocAnnotation> annotations) {
        Objects.requireNonNull(tableName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        TableBuilder tableBuilder = schema.addTable(tableName, annotations);
        this.currentTableBuilder = tableBuilder;
        return this;
    }

    @Override
    public DatabaseSpecBuilder alterTable(String schemaName, String tableName) {
        Objects.requireNonNull(tableName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        TableBuilder tableBuilder = schema.getTableToAlter(tableName);
        this.currentTableBuilder = tableBuilder;
        return this;
    }

    @Override
    public DatabaseSpecBuilder addTableColumn(String columnName, DataType dataType, List<DocAnnotation> annotations) {
        Objects.requireNonNull(columnName);
        Objects.requireNonNull(dataType);
        ColumnBuilder builder = requireCurrentTable().addColumn(columnName, dataType, annotations);
        this.currentColumnBuilder = builder;
        return this;
    }

    @Override
    public DatabaseSpecBuilder addTablePrimaryKey(String constraintName, List<String> columnNames) {
        requireCurrentTable().setPrimaryKey(constraintName, columnNames);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addTableForeignKey(
        String constraintName,
        SchemaObjectRef table,
        List<String> columns,
        List<String> refColumns) {

        Objects.requireNonNull(table);
        Objects.requireNonNull(columns);

        var columnMapping = new LinkedHashMap<String, String>();
        for (int i = 0; i < columns.size(); i++) {
            columnMapping.put(columns.get(i), refColumns.get(i));
        }
        requireCurrentTable().addForeignKey(constraintName, table, columnMapping);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addTableUniqueConstraint(
        String constraintName, List<String> columnNames) {
        requireCurrentTable().addUnique(constraintName, columnNames);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addColumnNullable(boolean isNullable) {
        requireCurrentColumn().setRequired(!isNullable);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addColumnDefaultValue(Expression defaultValue) {
        Objects.requireNonNull(defaultValue);
        requireCurrentColumn().setDefaultValue(defaultValue);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations) {
        Objects.requireNonNull(sequenceName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        SequenceBuilder builder = schema.addSequence(sequenceName, annotations);
        this.currentSequenceBuilder = builder;
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequenceDataType(IntegerDataType dataType) {
        Objects.requireNonNull(dataType);
        requireCurrentSequence().setDataType(dataType);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequenceStart(long start) {
        requireCurrentSequence().setStart(start);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequenceIncrement(long increment) {
        requireCurrentSequence().setIncrement(increment);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequenceMaxValue(long maxValue) {
        requireCurrentSequence().setMaxValue(maxValue);
        return this;
    }

    @Override
    public DatabaseSpecBuilder addSequenceMinValue(long minValue) {
        requireCurrentSequence().setMinValue(minValue);
        return this;
    }

    @Override
    public DatabaseSpec build() {
        var databases = this.databaseBuilders.values().stream()
                .map(DatabaseBuilder::build).toList();
        return new DefaultDatabaseSpec(
                Optional.ofNullable(title), databases);
    }

    //

    private DatabaseBuilder findDatabase(String name) {
        var database = databaseBuilders.get(name);
        if (database == null) {
            database =  addUndefinedDatabase(name);
        }
        return database;
    }

    private DatabaseBuilder addUndefinedDatabase(String name) {
        return addAnnotatedDatabase(name, Collections.emptyList(), State.UNDEFINED);
    }

    private DatabaseBuilder addAnnotatedDatabase(String name, List<DocAnnotation> annotations, State state) {
        var builder = new DatabaseBuilder(name, annotations, state);
        databaseBuilders.put(name, builder);
        return builder;
    }

    private TableBuilder requireCurrentTable() {
        if (this.currentTableBuilder == null) {
            throw new IllegalStateException("No current table");
        }
        return this.currentTableBuilder;
    }

    private ColumnBuilder requireCurrentColumn() {
        if (this.currentColumnBuilder == null) {
            throw new IllegalStateException("No current column");
        }
        return this.currentColumnBuilder;
    }

    private SequenceBuilder requireCurrentSequence() {
        if (this.currentSequenceBuilder == null) {
            throw new IllegalStateException("No current sequence");
        }
        return this.currentSequenceBuilder;
    }
}
