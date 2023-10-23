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

import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.datatype.IntegerDataType;
import dev.openclosed.squall.api.sql.expression.ObjectRef;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Database specification.
 * @param metadata the metadata of the specification.
 * @param databases the list of the databases defined in this specification.
 */
public record DatabaseSpec(
    Optional<SpecMetadata> metadata,
    List<Database> databases
    ) {

    public DatabaseSpec {
        Objects.requireNonNull(metadata);
        Objects.requireNonNull(databases);
        databases = List.copyOf(databases);
    }

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
     * A builder of database specification.
     */
    public interface Builder {

        default Builder setMetadata(SpecMetadata metadata) {
            return this;
        }

        default Builder addDatabase(String name, List<DocAnnotation> annotations) {
            return this;
        }

        default Builder changeCurrentDatabase(String name) {
            return this;
        }

        default Builder addSchema(String name, List<DocAnnotation> annotations) {
            return this;
        }

        // Table

        default Builder addTable(String schemaName, String tableName, List<DocAnnotation> annotations) {
            return this;
        }

        default Builder alterTable(String schemaName, String tableName) {
            return this;
        }

        default Builder addTableColumn(String columnName, DataType dataType, List<DocAnnotation> annotations) {
            return this;
        }

        default Builder addTablePrimaryKey(String constraintName, List<String> columnNames) {
            return this;
        }

        default Builder addTableForeignKey(
            String constraintName,
            ObjectRef tableRef,
            List<String> columns,
            List<String> refColumns) {
            return this;
        }

        default Builder addTableUniqueConstraint(String constraintName, List<String> columnNames) {
            return this;
        }

        // Column

        default Builder addColumnNullable(boolean isNullable) {
            return this;
        }

        default Builder addColumnDefaultValue(Expression defaultValue) {
            return this;
        }

        // Sequence

        default Builder addSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations) {
            return this;
        }

        default Builder addSequenceDataType(IntegerDataType dataType) {
            return this;
        }

        default Builder addSequenceStart(long start) {
            return this;
        }

        default Builder addSequenceIncrement(long increment) {
            return this;
        }

        default Builder addSequenceMaxValue(long maxValue) {
            return this;
        }

        default Builder addSequenceMinValue(long minValue) {
            return this;
        }

        default DatabaseSpec build() {
            throw new UnsupportedOperationException();
        }
    }
}
