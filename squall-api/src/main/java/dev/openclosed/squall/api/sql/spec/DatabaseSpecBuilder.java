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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import dev.openclosed.squall.api.sql.annotation.DocAnnotation;
import dev.openclosed.squall.api.sql.expression.ObjectRef;
import dev.openclosed.squall.api.sql.expression.Typecast;
import dev.openclosed.squall.api.sql.spec.Component.State;
import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.datatype.IntegerDataType;

/**
 * The default implementation of {@link DatabaseSpec.Builder}.
 */
final class DatabaseSpecBuilder implements DatabaseSpec.Builder {

    private final Map<String, DatabaseBuilder> databaseBuilders = new LinkedHashMap<>();
    private SpecMetadata metadata;
    private DatabaseBuilder currentDatabaseBuilder;
    private TableBuilder currentTableBuilder;
    private ColumnBuilder currentColumnBuilder;
    private SequenceBuilder currentSequenceBuilder;

    DatabaseSpecBuilder() {
    }

    @Override
    public DatabaseSpec.Builder setMetadata(SpecMetadata metadata) {
        Objects.requireNonNull(metadata);
        this.metadata = metadata;
        return this;
    }

    @Override
    public DatabaseSpec.Builder addDatabase(String name, List<DocAnnotation<?>> annotations) {
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
    public DatabaseSpec.Builder addSchema(String name, List<DocAnnotation<?>> annotations) {
        Objects.requireNonNull(name);
        getCurrentDatabase().addSchema(name, annotations);
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTable(String schemaName, String tableName, List<DocAnnotation<?>> annotations) {
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
    public DatabaseSpec.Builder addTableColumn(
        String columnName, DataType dataType, List<DocAnnotation<?>> annotations) {
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
    public DatabaseSpec.Builder addSequence(
        String schemaName, String sequenceName, List<DocAnnotation<?>> annotations) {
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
        return new DatabaseSpec(
            Optional.ofNullable(metadata),
            databases);
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

    private DatabaseBuilder addAnnotatedDatabase(String name, List<DocAnnotation<?>> annotations, State state) {
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

abstract class ComponentBuilder {

    private final String name;
    private final List<String> parents;
    private final List<String> parentsForChild;
    private final List<DocAnnotation<?>> annotations;

    protected ComponentBuilder(String name, List<String> parents, List<DocAnnotation<?>> annotations) {
        this.name = name;
        this.parents = parents;
        this.parentsForChild = concatNameList(parents, name);
        this.annotations = annotations;
    }

    final String name() {
        return name;
    }

    final List<String> parents() {
        return parents;
    }

    final List<String> parentsForChild() {
        return parentsForChild;
    }

    final List<DocAnnotation<?>> annotations() {
        return annotations;
    }

    private static List<String> concatNameList(List<String> parents, String name) {
        if (parents.isEmpty()) {
            return List.of(name);
        } else {
            return Stream.concat(parents.stream(), Stream.of(name)).toList();
        }
    }
}

final class DatabaseBuilder extends ComponentBuilder {

    private final Component.State state;
    private final Map<String, SchemaBuilder> schemaBuilders = new LinkedHashMap<>();

    DatabaseBuilder(String name, List<DocAnnotation<?>> annotations, Component.State state) {
        super(name, Collections.emptyList(), annotations);
        this.state = state;
    }

    Database build() {
        var schemas = this.schemaBuilders.values().stream()
            .map(SchemaBuilder::build).toList();
        return new Database(name(), schemas, annotations(), state);
    }

    void addSchema(String name, List<DocAnnotation<?>> annotations) {
        addAnnotatedSchema(name, annotations, Component.State.DEFINED);
    }

    SchemaBuilder getSchema(String name) {
        var builder = schemaBuilders.get(name);
        if (builder == null) {
            builder = addAnnotatedSchema(name, Collections.emptyList(), Component.State.UNDEFINED);
        }
        return builder;
    }

    private SchemaBuilder addAnnotatedSchema(String name, List<DocAnnotation<?>> annotations, Component.State state) {
        var builder = new SchemaBuilder(name, parentsForChild(), annotations, state);
        schemaBuilders.put(name, builder);
        return builder;
    }
}

final class SchemaBuilder extends ComponentBuilder {

    private final Component.State state;
    private final Map<String, TableBuilder> tableBuilders = new LinkedHashMap<>();
    private final Map<String, SequenceBuilder> sequenceBuilders = new LinkedHashMap<>();

    SchemaBuilder(String name, List<String> parents, List<DocAnnotation<?>> annotations, Component.State state) {
        super(name, parents, annotations);
        this.state = state;
    }

    Schema build() {
        var sequences = this.sequenceBuilders.values().stream()
            .map(SequenceBuilder::build).toList();
        var tables = this.tableBuilders.values().stream()
            .map(TableBuilder::build).toList();
        return new Schema(name(), parents(), sequences, tables, annotations(), state);
    }

    TableBuilder addTable(String tableName, List<DocAnnotation<?>> annotations) {
        var builder = new TableBuilder(tableName, parentsForChild(), annotations);
        tableBuilders.put(tableName, builder);
        return builder;
    }

    TableBuilder getTableToAlter(String name) {
        return tableBuilders.get(name);
    }

    SequenceBuilder addSequence(String sequenceNmae, List<DocAnnotation<?>> annotations) {
        var builder = new SequenceBuilder(sequenceNmae, parentsForChild(), annotations);
        sequenceBuilders.put(sequenceNmae, builder);
        return builder;
    }
}

final class SequenceBuilder extends ComponentBuilder {

    private IntegerDataType dataType = IntegerDataType.BIGINT;
    private long increment = 1L;
    private Long startValue;
    private Long maxValue;
    private Long minValue;

    SequenceBuilder(String name, List<String> parents, List<DocAnnotation<?>> annotations) {
        super(name, parents, annotations);
    }

    Sequence build() {
        final long min = computeMinValue();
        final long max = computeMaxValue();
        final long start = computeStart(min, max);
        return new Sequence(
            name(),
            parents(),
            dataType.typeName(),
            start,
            this.increment,
            max,
            min,
            annotations());
    }

    void setDataType(IntegerDataType dataType) {
        this.dataType = dataType;
    }

    void setStart(long start) {
        this.startValue = start;
    }

    void setIncrement(long increment) {
        this.increment = increment;
    }

    void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    private long computeMaxValue() {
        if (maxValue != null) {
            return maxValue;
        } else if (increment < 0) {
            return -1L;
        } else {
            return dataType.maxValue();
        }
    }

    private long computeMinValue() {
        if (minValue != null) {
            return minValue;
        } else if (increment < 0) {
            return dataType.minValue();
        } else {
            return 1L;
        }
    }

    private long computeStart(long minValue, long maxValue) {
        if (startValue != null) {
            return startValue;
        } else if (increment < 0) {
            return maxValue;
        } else {
            return minValue;
        }
    }
}

final class TableBuilder extends ComponentBuilder {

    private final Map<String, ColumnBuilder> columns = new LinkedHashMap<>();
    private PrimaryKey primaryKey;
    private final List<Unique> unique = new ArrayList<>();
    private final List<ForeignKey> foreignKeys = new ArrayList<>();

    TableBuilder(String name, List<String> parents, List<DocAnnotation<?>> annotations) {
        super(name, parents, annotations);
    }

    Table build() {
        return new Table(
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
                            List<DocAnnotation<?>> annotations) {
        var builder = new ColumnBuilder(name, parentsForChild(), dataType, annotations);
        this.columns.put(name, builder);
        return builder;
    }

    void setPrimaryKey(String name, List<String> columnNames) {
        this.primaryKey = new PrimaryKey(Optional.ofNullable(name), columnNames);
    }

    void addUnique(String name, List<String> columns) {
        unique.add(new Unique(Optional.ofNullable(name), columns));
    }

    void addForeignKey(String name, ObjectRef tableRef, List<String> columns, List<String> refColumns) {
        var columnMapping = new LinkedHashMap<String, String>();
        for (int i = 0; i < columns.size(); i++) {
            columnMapping.put(columns.get(i), refColumns.get(i));
        }

        foreignKeys.add(new ForeignKey(
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

final class ColumnBuilder extends ComponentBuilder {

    private final DataType dataType;
    private boolean isPrimaryKey = false;
    private boolean isRequired = false;
    private boolean isUnique = false;
    private Expression defaultValue;

    ColumnBuilder(String name, List<String> parents, DataType dataType, List<DocAnnotation<?>> annotations) {
        super(name, parents, annotations);
        this.dataType = dataType;
    }

    Column build() {
        var simplifiedValue = simplifyDefaultValue(this.defaultValue, this.dataType);
        return new Column(
            name(),
            parents(),
            dataType.typeName(),
            dataType.length(),
            dataType.precision(),
            dataType.scale(),
            isRequired,
            isPrimaryKey,
            isUnique,
            Optional.ofNullable(simplifiedValue),
            annotations());
    }

    void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
        this.isRequired = true;
    }

    void setRequired(boolean isRequired) {
        if (!isPrimaryKey) {
            this.isRequired = isRequired;
        }
    }

    void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    void setDefaultValue(Expression value) {
        this.defaultValue = value;
    }

    private static Expression simplifyDefaultValue(Expression value, DataType targetType) {
        if (!(value instanceof Typecast typecast)) {
            return value;
        }
        Expression source = typecast.source();
        return switch (source.type()) {
            case NULL -> Expression.NULL;
            case STRING -> {
                if (typecast.typeName().equals(targetType.typeName())
                    && typecast.length().isEmpty()
                    && typecast.precision().isEmpty()) {
                    yield source;
                } else {
                    yield value;
                }
            }
            default -> value;
        };
    }
}
