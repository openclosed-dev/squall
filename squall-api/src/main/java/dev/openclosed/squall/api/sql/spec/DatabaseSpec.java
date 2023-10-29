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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Database design specification.
 * @param metadata the metadata of the specification.
 * @param databases the list of the databases defined in this specification.
 */
public record DatabaseSpec(
    Optional<SpecMetadata> metadata,
    List<Database> databases
    ) {

    /**
     * Constructs the specification.
     * @param metadata the metadata of the specification.
     * @param databases the list of the databases defined in this specification.
     */
    public DatabaseSpec {
        Objects.requireNonNull(metadata);
        Objects.requireNonNull(databases);
        databases = List.copyOf(databases);
    }

    /**
     * Returns the metadata of the specification.
     * @return the metadata of the specification, or default one.
     */
    public SpecMetadata getMetadataOrDefault() {
        return metadata().orElse(SpecMetadata.DEFAULT);
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
     * Builder of a database specification.
     */
    public interface Builder {

        /**
         * Assigns metadata of the specification.
         * @param metadata the metadata
         * @return this builder.
         */
        default Builder setMetadata(SpecMetadata metadata) {
            return this;
        }

        /**
         * Adds a database.
         * @param name the name of the database.
         * @param annotations the annotations attached to the database.
         * @return this builder.
         */
        default Builder addDatabase(String name, List<DocAnnotation> annotations) {
            return this;
        }

        /**
         * Changes the current database.
         * @param name the name ot the database.
         * @return this builder.
         */
        default Builder changeCurrentDatabase(String name) {
            return this;
        }

        /**
         * Adds a schema.
         * @param name the name of the schema.
         * @param annotations the annotations attached to the schema.
         * @return this builder.
         */
        default Builder addSchema(String name, List<DocAnnotation> annotations) {
            return this;
        }

        // Table

        /**
         * Adds a table.
         * @param schemaName the name of the schema that contains the table.
         * @param tableName the name of the table.
         * @param annotations the annotations attached to the table.
         * @return this builder.
         */
        default Builder addTable(String schemaName, String tableName, List<DocAnnotation> annotations) {
            return this;
        }

        /**
         * Alters a table.
         * @param schemaName the name of the schema that contains the table.
         * @param tableName the name of the table.
         * @return this builder.
         */
        default Builder alterTable(String schemaName, String tableName) {
            return this;
        }

        /**
         * Adds a column in a table.
         * @param columnName the name of the column.
         * @param dataType the data type of the column.
         * @param annotations the annotations attached to the column.
         * @return this builder.
         */
        default Builder addTableColumn(String columnName, DataType dataType, List<DocAnnotation> annotations) {
            return this;
        }

        /**
         * Adds a primary key to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param columnNames the columns composing the primary key.
         * @return this builder.
         */
        default Builder addTablePrimaryKey(String constraintName, List<String> columnNames) {
            return this;
        }

        /**
         * Adds a foreign key to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param tableRef the referenced table.
         * @param columns the referencing columns.
         * @param refColumns the referenced columns.
         * @return this builder.
         */
        default Builder addTableForeignKey(
            String constraintName,
            ObjectRef tableRef,
            List<String> columns,
            List<String> refColumns) {
            return this;
        }

        /**
         * Adds a unique constraint to the current table.
         * @param constraintName the name of the constraint, may be {@code null}.
         * @param columnNames the list of columns.
         * @return this builder.
         */
        default Builder addTableUniqueConstraint(String constraintName, List<String> columnNames) {
            return this;
        }

        // Column

        /**
         * Specifies whether the column is nullable or not.
         * @param isNullable {@code true} if the column is nullable, {@code false} otherwise.
         * @return this builder.
         */
        default Builder addColumnNullable(boolean isNullable) {
            return this;
        }

        /**
         * Specifies default value to the current column.
         * @param defaultValue the expression representing the default value of the column.
         * @return this builder.
         */
        default Builder addColumnDefaultValue(Expression defaultValue) {
            return this;
        }

        // Sequence

        /**
         * Adds a sequence.
         * @param schemaName the name of the schema that contains the table.
         * @param sequenceName the name of the sequence.
         * @param annotations the annotations attached to the sequence.
         * @return this builder.
         */
        default Builder addSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations) {
            return this;
        }

        /**
         * Specifies the data type of the current sequence.
         * @param dataType the data type of the sequence.
         * @return this builder.
         */
        default Builder addSequenceDataType(IntegerDataType dataType) {
            return this;
        }

        /**
         * Specifies the staring value of the current sequence.
         * @param start the staring value.
         * @return this builder.
         */
        default Builder addSequenceStart(long start) {
            return this;
        }

        /**
         * Specifies the increment value of the current sequence.
         * @param increment the increment value.
         * @return this builder.
         */
        default Builder addSequenceIncrement(long increment) {
            return this;
        }

        /**
         * Specifies the maximum value of the current sequence.
         * @param maxValue the maximum value.
         * @return this builder.
         */
        default Builder addSequenceMaxValue(long maxValue) {
            return this;
        }

        /**
         * Specifies the minimum value of the current sequence.
         * @param minValue the minimum value.
         * @return this builder.
         */
        default Builder addSequenceMinValue(long minValue) {
            return this;
        }

        /**
         * Builds a database specification.
         * @return built database specification.
         */
        default DatabaseSpec build() {
            throw new UnsupportedOperationException();
        }
    }
}
