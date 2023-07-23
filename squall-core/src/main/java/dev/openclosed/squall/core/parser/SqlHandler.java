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

import java.util.List;

public interface SqlHandler {

    default void handleDatabase(String name) {
    }

    default void handleSchema(String name) {
    }

    default void handleTable(String schemaName, String tableName) {
    }

    default void handleTableToAlter(String schemaName, String tableName) {
    }

    default void handleTablePrimaryKey(String constraintName, List<String> columns) {
    }

    default void handleTableUniqueConstraint(String constraintName, List<String> columns) {
    }

    default void handleColumn(String name, DataType dataType) {
    }

    default void handleColumnDefaultValue(Expression devaultValue) {
    }

    default void handleColumnNullable(boolean isNullable) {
    }

    default void handleSequence(String schemaName, String sequenceName) {
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
