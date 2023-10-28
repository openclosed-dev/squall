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

package dev.openclosed.squall.api.sql.expression;

import java.util.Objects;

/**
 * Column reference.
 * @param name the name of the referenced column.
 */
public record ColumnReference(String name) implements Expression {

    /**
     * Creates an instance of a {@code ColumnReference} record class.
     * @param name the name of the referenced column.
     */
    public ColumnReference {
        Objects.requireNonNull(name);
    }

    @Override
    public Type type() {
        return Type.COLUMN_REFERENCE;
    }

    @Override
    public String toSql() {
        return name();
    }
}
