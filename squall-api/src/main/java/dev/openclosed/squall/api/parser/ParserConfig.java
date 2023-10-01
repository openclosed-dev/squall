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

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.api.sql.spec.MajorDialect;

/**
 * A configuration for SQL parsers.
 * @param dialect the dialect of the SQL.
 * @param defaultSchema the name of the default schema.
 */
public record ParserConfig(
        String dialect,
        String defaultSchema) {

    private static final ParserConfig DEFAULT = new ParserConfig();

    public static ParserConfig getDefault() {
        return DEFAULT;
    }

    public ParserConfig() {
        this(MajorDialect.POSTGRESQL);
    }

    public ParserConfig(Dialect dialect) {
        this(dialect.dialectName(), dialect.defaultSchema());
    }
}
