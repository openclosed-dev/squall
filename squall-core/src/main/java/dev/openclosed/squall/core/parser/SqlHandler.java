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

package dev.openclosed.squall.core.parser;

import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.SchemaObjectRef;

import java.util.List;

public interface SqlHandler {

    default void handleDatabase(String name, List<DocAnnotation> annotations) {
    }

    default void handleSchema(String name, List<DocAnnotation> annotations) {
    }

    default void handleTable(String schemaName, String tableName, List<DocAnnotation> annotations) {
    }

    default void handleTableToAlter(String schemaName, String tableName) {
    }

    default void handleTablePrimaryKey(String constraintName, List<String> columns) {
    }

    default void handleTableForeignKey(
            String constraintName, SchemaObjectRef table, List<String> columns, List<String> refColumns) {
    }

    default void handleTableUniqueConstraint(String constraintName, List<String> columns) {
    }

    default void handleCheckConstraint(String constraintName, Expression expression) {
    }

    default void handleColumn(String name, DataType dataType, List<DocAnnotation> annotations) {
    }

    default void handleColumnDefaultValue(Expression defaultValue) {
    }

    default void handleColumnNullable(boolean isNullable) {
    }

    default void handleSequence(String schemaName, String sequenceName, List<DocAnnotation> annotations) {
    }

    default void handleSequenceDataType(IntegerDataType dataType) {
    }

    default void handleSequenceStart(long start) {
    }

    default void handleSequenceIncrement(long increment) {
    }

    default void handleSequenceMaxValue(long maxValue) {
    }

    default void handleSequenceMinValue(long minValue) {
    }
}
