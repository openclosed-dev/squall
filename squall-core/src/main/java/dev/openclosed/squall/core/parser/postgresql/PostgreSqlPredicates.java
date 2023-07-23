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

package dev.openclosed.squall.core.parser.postgresql;

import dev.openclosed.squall.core.parser.Keyword;
import dev.openclosed.squall.core.parser.SqlPredicates;

public interface PostgreSqlPredicates extends SqlPredicates {

    @Override
    default boolean testTemporary(Keyword keyword) {
        return keyword == PostgreSqlKeyword.TEMP || keyword == PostgreSqlKeyword.TEMPORARY;
    }

    @Override
    default boolean testSchemaObjectModifier(Keyword keyword) {
        return switch ((PostgreSqlKeyword) keyword) {
            case TEMP, TEMPORARY, LOCAL, GLOBAL, UNLOGGED -> true;
            default -> false;
        };
    }
}
