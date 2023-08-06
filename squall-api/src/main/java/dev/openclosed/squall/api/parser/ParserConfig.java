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

import java.util.Optional;

import dev.openclosed.squall.api.spec.Dialect;

public record ParserConfig(
        String dialect,
        Optional<String> defaultSchema) {

    private static final ParserConfig DEFAULT = new ParserConfig();

    public static ParserConfig getDefault() {
        return DEFAULT;
    }

    public ParserConfig() {
        this(Dialect.POSTGRESQL.dialectName(), Optional.empty());
    }

    public String getDefaultSchema() {
        return defaultSchema().orElseGet(() ->
            Dialect.find(dialect()).map(Dialect::defaultSchema).orElse("")
        );
    }
}
