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

package dev.openclosed.squall.api.spec.builder;

import java.util.List;
import java.util.ServiceLoader;

import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.SchemaObjectRef;

/**
 * A builder of database specification.
 */
public interface DatabaseSpecBuilder {

    /**
     * Creates a new builder.
     * @return created builder instance.
     */
    static DatabaseSpecBuilder newBuilder() {
        return ServiceLoader.load(DatabaseSpecBuilder.class).findFirst().get();
    }

    DatabaseSpecBuilder setTitle(String title);

    DatabaseSpecBuilder addDatabase(String name, List<DocAnnotation> annotations);

    DatabaseSpecBuilder changeCurrentDatabase(String name);

    DatabaseSpecBuilder addSchema(String name, List<DocAnnotation> annotations);

    // Table

    DatabaseSpecBuilder addTable(String schemaName, String tableName, List<DocAnnotation> annotations);

    DatabaseSpecBuilder alterTable(String schemaName, String tableName);

    DatabaseSpecBuilder addTableColumn(String columnName, DataType dataType, List<DocAnnotation> annotations);

    DatabaseSpecBuilder addTablePrimaryKey(String constraintName, List<String> columnNames);

    DatabaseSpecBuilder addTableForeignKey(
        String constraintName,
        SchemaObjectRef table,
        List<String> columns,
        List<String> refColumns);

    DatabaseSpecBuilder addTableUniqueConstraint(String constraintName, List<String> columnNames);

    // Column

    DatabaseSpecBuilder addColumnNullable(boolean isNullable);

    DatabaseSpecBuilder addColumnDefaultValue(Expression defaultValue);

    // Sequence

    DatabaseSpecBuilder addSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations);

    DatabaseSpecBuilder addSequenceDataType(IntegerDataType dataType);

    DatabaseSpecBuilder addSequenceStart(long start);

    DatabaseSpecBuilder addSequenceIncrement(long increment);

    DatabaseSpecBuilder addSequenceMaxValue(long maxValue);

    DatabaseSpecBuilder addSequenceMinValue(long minValue);

    DatabaseSpec build();
}
