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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import dev.openclosed.squall.api.sql.expression.ObjectRef;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.DocAnnotation;
import dev.openclosed.squall.api.sql.spec.Component.State;
import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.expression.ExpressionFactory;
import dev.openclosed.squall.api.sql.datatype.IntegerDataType;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;
import dev.openclosed.squall.core.sql.spec.DefaultDatabaseSpec;
import dev.openclosed.squall.core.sql.expression.DefaultExpressionFactory;

/**
 * The default implementation of {@link DatabaseSpec.Builder}.
 */
public final class DatabaseSpecBuilder implements DatabaseSpec.Builder {

    private final ExpressionFactory expressionFactory;
    private final Map<String, DatabaseBuilder> databaseBuilders = new LinkedHashMap<>();
    private SpecMetadata metadata;
    private DatabaseBuilder currentDatabaseBuilder;
    private TableBuilder currentTableBuilder;
    private ColumnBuilder currentColumnBuilder;
    private SequenceBuilder currentSequenceBuilder;

    public DatabaseSpecBuilder() {
        this.expressionFactory = new DefaultExpressionFactory();
    }

    @Override
    public DatabaseSpec.Builder setMetadata(SpecMetadata metadata) {
        Objects.requireNonNull(metadata);
        this.metadata = metadata;
        return this;
    }

    @Override
    public DatabaseSpec.Builder addDatabase(String name, List<DocAnnotation> annotations) {
        Objects.requireNonNull(name);
        addAnnotatedDatabase(name, annotations, State.DEFINED);
        changeCurrentDatabase(name);
        return this;
    }

    @Override
    public DatabaseSpec.Builder changeCurrentDatabase(String name) {
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
    public DatabaseSpec.Builder addSchema(String name, List<DocAnnotation> annotations) {
        Objects.requireNonNull(name);
        getCurrentDatabase().addSchema(name, annotations);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTable(String schemaName, String tableName, List<DocAnnotation> annotations) {
        Objects.requireNonNull(tableName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        TableBuilder tableBuilder = schema.addTable(tableName, annotations);
        this.currentTableBuilder = tableBuilder;
        return this;
    }

    @Override
    public DatabaseSpec.Builder alterTable(String schemaName, String tableName) {
        Objects.requireNonNull(tableName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        TableBuilder tableBuilder = schema.getTableToAlter(tableName);
        this.currentTableBuilder = tableBuilder;
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableColumn(String columnName, DataType dataType, List<DocAnnotation> annotations) {
        Objects.requireNonNull(columnName);
        Objects.requireNonNull(dataType);
        ColumnBuilder builder = requireCurrentTable().addColumn(columnName, dataType, annotations);
        this.currentColumnBuilder = builder;
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTablePrimaryKey(String constraintName, List<String> columnNames) {
        requireCurrentTable().setPrimaryKey(constraintName, columnNames);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableForeignKey(
        String constraintName,
        ObjectRef tableRef,
        List<String> columns,
        List<String> refColumns) {
        Objects.requireNonNull(tableRef);
        Objects.requireNonNull(columns);
        Objects.requireNonNull(refColumns);
        requireCurrentTable().addForeignKey(constraintName, tableRef, columns, refColumns);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableUniqueConstraint(
        String constraintName, List<String> columnNames) {
        requireCurrentTable().addUnique(constraintName, columnNames);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addColumnNullable(boolean isNullable) {
        requireCurrentColumn().setRequired(!isNullable);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addColumnDefaultValue(Expression defaultValue) {
        Objects.requireNonNull(defaultValue);
        requireCurrentColumn().setDefaultValue(defaultValue);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations) {
        Objects.requireNonNull(sequenceName);
        SchemaBuilder schema = getCurrentDatabase().getSchema(schemaName);
        SequenceBuilder builder = schema.addSequence(sequenceName, annotations);
        this.currentSequenceBuilder = builder;
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceDataType(IntegerDataType dataType) {
        Objects.requireNonNull(dataType);
        requireCurrentSequence().setDataType(dataType);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceStart(long start) {
        requireCurrentSequence().setStart(start);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceIncrement(long increment) {
        requireCurrentSequence().setIncrement(increment);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceMaxValue(long maxValue) {
        requireCurrentSequence().setMaxValue(maxValue);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceMinValue(long minValue) {
        requireCurrentSequence().setMinValue(minValue);
        return this;
    }

    @Override
    public DatabaseSpec build() {
        var databases = this.databaseBuilders.values().stream()
                .map(DatabaseBuilder::build).toList();
        return new DefaultDatabaseSpec(
            Optional.ofNullable(metadata),
            databases);
    }

    @Override
    public ExpressionFactory getExpressionFactory() {
        return this.expressionFactory;
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
