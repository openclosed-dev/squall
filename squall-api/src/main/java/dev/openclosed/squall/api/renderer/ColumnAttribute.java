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

package dev.openclosed.squall.api.renderer;

import java.util.List;

/**
 * Attributes of a column in a table.
 */
public enum ColumnAttribute {
    /** Ordinal number in the table. */
    ORDINAL,
    /** Physical name of the column. */
    NAME,
    /** Name for display purpose. */
    LABEL,
    /** Name of the data type with length, precision, and/or scale. */
    TYPE,
    /** Name of the data type. */
    TYPE_NAME,
    /** Precision or length of the data type. */
    PRECISION_LENGTH,
    /** Scale of the data type. */
    SCALE,
    /** Whether the column is nullable or not. */
    NULLABLE,
    /** Whether the column is required or not. */
    REQUIRED,
    /** Unique constraint of the column. */
    UNIQUE,
    /** Default value of the column. */
    DEFAULT_VALUE,
    /** Foreign key constraints of the column. */
    FOREIGN_KEY,
    /** Description of the column. */
    DESCRIPTION;

    private static final List<ColumnAttribute> DEFAULT_LIST = List.of(
        ORDINAL,
        NAME,
        LABEL,
        TYPE,
        NULLABLE,
        UNIQUE,
        DEFAULT_VALUE,
        FOREIGN_KEY,
        DESCRIPTION
    );

    /**
     * Returns the default list of the attributes.
     * @return the default list of the attributes.
     */
    public static List<ColumnAttribute> defaultList() {
        return DEFAULT_LIST;
    }
}
