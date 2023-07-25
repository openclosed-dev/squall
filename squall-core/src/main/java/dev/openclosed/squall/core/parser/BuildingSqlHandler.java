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
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.IntegerDataType;
import dev.openclosed.squall.api.spec.TableRef;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

import java.util.List;

public interface BuildingSqlHandler extends SqlHandler {

    DatabaseSpecBuilder builder();

    @Override
    default void handleDatabase(String name) {
        builder().addDatabase(name);
    }

    @Override
    default void handleSchema(String name) {
        builder().addSchema(name);
    }

    @Override
    default void handleTable(String schemaName, String tableName) {
        builder().addTable(schemaName, tableName);
    }

    @Override
    default void handleTableToAlter(String schemaName, String tableName) {
        builder().alterTable(schemaName, tableName);
    }

    @Override
    default void handleTablePrimaryKey(String constraintName, List<String> columns) {
        builder().addTablePrimaryKey(constraintName, columns);
    }

    @Override
    default void handleTableForeignKey(
        String constraintName, TableRef table, List<String> columns, List<String> refColumns) {
        builder().addTableForeignKey(constraintName, table, columns, refColumns);
    }

    @Override
    default void handleTableUniqueConstraint(String constraintName, List<String> columns) {
        builder().addTableUniqueConstraint(constraintName, columns);
    }

    @Override
    default void handleColumn(String name, DataType dataType) {
        builder().addTableColumn(name, dataType);
    }

    @Override
    default void handleColumnDefaultValue(Expression defaultValue) {
        builder().addColumnDefaultValue(defaultValue);
    }

    @Override
    default void handleColumnNullable(boolean isNullable) {
        builder().addColumnNullable(isNullable);
    }

    @Override
    default void handleSequence(String schemaName, String sequenceName) {
        builder().addSequence(schemaName, sequenceName);
    }

    @Override
    default void handleSequenceDataType(IntegerDataType dataType) {
        builder().addSequenceDataType(dataType);
    }

    @Override
    default void handleSequenceStart(long start) {
        builder().addSequenceStart(start);
    }

    @Override
    default void handleSequenceIncrement(long increment) {
        builder().addSequenceIncrement(increment);
    }

    @Override
    default void handleSequenceMaxValue(long maxValue) {
        builder().addSequenceMaxValue(maxValue);
    }

    @Override
    default void handleSequenceMinValue(long minValue) {
        builder().addSequenceMinValue(minValue);
    }

}
