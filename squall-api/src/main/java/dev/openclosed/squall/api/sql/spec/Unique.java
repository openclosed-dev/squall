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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Unique constraint.
 * @param constraintName the name of the constraint, optional.
 * @param columns the list of columns composing this unique key.
 */
public record Unique(Optional<String> constraintName, List<String> columns)
    implements Constraint {

    /**
     * Creates an instance of a {@code Unique} record class.
     * @param constraintName the name of the constraint, optional.
     * @param columns the list of columns composing this unique key.
     */
    public Unique {
        Objects.requireNonNull(columns);
        if (columns.isEmpty()) {
            throw new IllegalArgumentException();
        }
        columns = List.copyOf(columns);
    }

    /**
     * Returns whether this constraint contains the specified column or not.
     * @param name the name of the column to test.
     * @return {@code true} if this constraint contains the specified column, {@code false} otherwise.
     */
    public boolean containsColumn(String name) {
        return columns.contains(name);
    }

    /**
     * Returns whether this constraint contains multiple columns or not.
     * @return {@code true} if this constraint contains multiple columns,
     *         {@code false} if this constraint contains a single column.
     */
    public boolean isComposite() {
        return columns.size() > 1;
    }
}
