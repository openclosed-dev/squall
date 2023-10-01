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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A foreign key constraint.
 */
public interface ForeignKey extends Constraint {

    List<String> tableName();

    /**
     * Returns the referenced table name.
     * @return the referenced table name.
     */
    default String simpleTableName() {
        var name = tableName();
        return name.get(name.size() - 1);
    }

    default String fullTableName() {
        return tableName().stream().collect(Collectors.joining("."));
    }

    Map<String, String> columnMapping();

    default boolean containsKey(String column) {
        return columnMapping().containsKey(column);
    }
}
