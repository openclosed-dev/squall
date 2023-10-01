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

package dev.openclosed.squall.core.sql.spec;

import dev.openclosed.squall.api.sql.spec.Unique;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Unique constraint.
 * @param constraintName the name of the constraint
 * @param columns the columns composing this unique key
 */
public record DefaultUnique(Optional<String> constraintName, List<String> columns)
    implements Unique {

    public DefaultUnique {
        Objects.requireNonNull(columns);
        if (columns.isEmpty()) {
            throw new IllegalArgumentException();
        }
        columns = List.copyOf(columns);
    }

    public boolean containsColumn(String name) {
        return columns.contains(name);
    }

    public boolean isComposite() {
        return columns.size() > 1;
    }
}
