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

import dev.openclosed.squall.api.sql.annotation.DocAnnotation;
import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.datatype.IntegerDataType;
import dev.openclosed.squall.api.sql.expression.ObjectRef;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Database design specification.
 * @param databases the list of the databases defined in this specification.
 * @param metadata the metadata of the specification.
 */
public record DatabaseSpec(
    List<Database> databases,
    SpecMetadata metadata
    ) {

    /**
     * Constructs the specification.
     * @param databases the list of the databases defined in this specification.
     * @param metadata the metadata of the specification.
     */
    public DatabaseSpec {
        Objects.requireNonNull(databases);
        Objects.requireNonNull(metadata);
        databases = List.copyOf(databases);
    }

    /**
     * Walks all components in a specification.
     * @param order visiting order.
     * @param visitor the component visitor.
     */
    public void walkSpec(ComponentOrder order, SpecVisitor visitor) {
            Objects.requireNonNull(visitor);
            databases().stream().forEach(c -> c.accept(visitor));
    }

    /**
     * Creates a new builder.
     * @return created builder instance.
     */
    public static Builder builder() {
        return new DatabaseSpecBuilder();
    }

    /**
     * Creates a builder of an empty specification.
     * @return created builder instance.
     */
    public static Builder emptyBuilder() {
        return EmptyDatabaseSpecBuilder.INSTANCE;
    }

    /**
     * Builder of a database specification.
     */
    public interface Builder {

        /**
         * Assigns metadata of the specification.
         * @param metadata the metadata, must not be {@code null}.
         * @return this builder.
         */
        Builder setMetadata(SpecMetadata metadata);

        /**
         * Assigns the title of the specification.
         * @param title the title of the specification, must not be {@code null}.
         * @return this builder.
         */
        Builder setTitle(String title);

        /**
         * Assigns the author of the specification.
         * @param author the author of the specification, can be {@code null}.
         * @return this builder.
         */
        Builder setAuthor(String author);

        /**
         * Assigns the version of the specification.
         * @param version the version of the specification, can be {@code null}.
         * @return this builder.
         */
        Builder setVersion(String version);

        /**
         * Assigns the release date of the specification.
         * @param date the release date of the specification, can be {@code null}.
         * @return this builder.
         */
        Builder setDate(String date);

        /**
         * Adds a database.
         * @param name the name of the database.
         * @param annotations the annotations attached to the database.
         * @return this builder.
         */
        Builder addDatabase(String name, List<DocAnnotation<?>> annotations);

        /**
         * Changes the current database.
         * @param name the name ot the database.
         * @return this builder.
         */
        Builder changeCurrentDatabase(String name);

        /**
         * Adds a schema.
         * @param name the name of the schema.
         * @param annotations the annotations attached to the schema.
         * @return this builder.
         */
        Builder addSchema(String name, List<DocAnnotation<?>> annotations);

        // Table

        /**
         * Adds a table.
         * @param schemaName the name of the schema that contains the table.
         * @param tableName the name of the table.
         * @param annotations the annotations attached to the table.
         * @return this builder.
         */
        Builder addTable(String schemaName, String tableName, List<DocAnnotation<?>> annotations);

        /**
         * Alters a table.
         * @param schemaName the name of the schema that contains the table.
         * @param tableName the name of the table.
         * @return this builder.
         */
        Builder alterTable(String schemaName, String tableName);

        /**
         * Adds a column in a table.
         * @param columnName the name of the column.
         * @param dataType the data type of the column.
         * @param annotations the annotations attached to the column.
         * @return this builder.
         */
        Builder addTableColumn(String columnName, DataType dataType, List<DocAnnotation<?>> annotations);

        /**
         * Adds a primary key to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param columnNames the columns composing the primary key.
         * @return this builder.
         */
        Builder addTablePrimaryKey(String constraintName, List<String> columnNames);

        /**
         * Adds a foreign key to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param tableRef the referenced table.
         * @param columns the referencing columns.
         * @param refColumns the referenced columns.
         * @return this builder.
         */
        Builder addTableForeignKey(
            String constraintName,
            ObjectRef tableRef,
            List<String> columns,
            List<String> refColumns);

        /**
         * Adds a unique constraint to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param columnNames the list of columns.
         * @return this builder.
         */
        Builder addTableUniqueConstraint(String constraintName, List<String> columnNames);

        // Column

        /**
         * Specifies whether the column is nullable or not.
         * @param isNullable {@code true} if the column is nullable, {@code false} otherwise.
         * @return this builder.
         */
        Builder addColumnNullable(boolean isNullable);

        /**
         * Specifies default value to the current column.
         * @param defaultValue the expression representing the default value of the column.
         * @return this builder.
         */
        Builder addColumnDefaultValue(Expression defaultValue);

        // Sequence

        /**
         * Adds a sequence.
         * @param schemaName the name of the schema that contains the table.
         * @param sequenceName the name of the sequence.
         * @param annotations the annotations attached to the sequence.
         * @return this builder.
         */
        Builder addSequence(String schemaName, String sequenceName, List<DocAnnotation<?>> annotations);

        /**
         * Specifies the data type of the current sequence.
         * @param dataType the data type of the sequence.
         * @return this builder.
         */
        Builder addSequenceDataType(IntegerDataType dataType);

        /**
         * Specifies the staring value of the current sequence.
         * @param start the staring value.
         * @return this builder.
         */
        Builder addSequenceStart(long start);

        /**
         * Specifies the increment value of the current sequence.
         * @param increment the increment value.
         * @return this builder.
         */
        Builder addSequenceIncrement(long increment);

        /**
         * Specifies the maximum value of the current sequence.
         * @param maxValue the maximum value.
         * @return this builder.
         */
        Builder addSequenceMaxValue(long maxValue);

        /**
         * Specifies the minimum value of the current sequence.
         * @param minValue the minimum value.
         * @return this builder.
         */
        Builder addSequenceMinValue(long minValue);

        /**
         * Builds a database specification.
         * @return built database specification.
         */
        DatabaseSpec build();
    }
}

final class EmptyDatabaseSpecBuilder implements DatabaseSpec.Builder {

    static final EmptyDatabaseSpecBuilder INSTANCE = new EmptyDatabaseSpecBuilder();

    @Override
    public DatabaseSpec.Builder setMetadata(SpecMetadata metadata) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder setTitle(String title) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder setAuthor(String author) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder setVersion(String version) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder setDate(String date) {
        return this;
    }


    @Override
    public DatabaseSpec.Builder addDatabase(String name, List<DocAnnotation<?>> annotations) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder changeCurrentDatabase(String name) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSchema(String name, List<DocAnnotation<?>> annotations) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTable(String schemaName, String tableName, List<DocAnnotation<?>> annotations) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder alterTable(String schemaName, String tableName) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableColumn(
        String columnName, DataType dataType, List<DocAnnotation<?>> annotations) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTablePrimaryKey(String constraintName, List<String> columnNames) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableForeignKey(
        String constraintName, ObjectRef tableRef, List<String> columns, List<String> refColumns) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addTableUniqueConstraint(String constraintName, List<String> columnNames) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addColumnNullable(boolean isNullable) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addColumnDefaultValue(Expression defaultValue) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequence(
        String schemaName, String sequenceName, List<DocAnnotation<?>> annotations) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceDataType(IntegerDataType dataType) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceStart(long start) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceIncrement(long increment) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceMaxValue(long maxValue) {
        return this;
    }

    @Override
    public DatabaseSpec.Builder addSequenceMinValue(long minValue) {
        return this;
    }

    @Override
    public DatabaseSpec build() {
        return new DatabaseSpec(
            Collections.emptyList(),
            SpecMetadata.DEFAULT);
    }
}
