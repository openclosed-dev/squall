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

import java.util.ServiceLoader;

import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

public interface SqlParserFactory {

    String dialectName();

    SqlParser createParser(
            ParserConfig config,
            DatabaseSpecBuilder builder,
            CommentHandler commentHandler);

    static SqlParserFactory get(Dialect dialect) {
        return get(dialect.dialectName());
    }

    static SqlParserFactory get(String dialectName) {
        for (var factory : ServiceLoader.load(SqlParserFactory.class)) {
            if (factory.dialectName().equals(dialectName)) {
                return factory;
            }
        }
        throw new IllegalArgumentException();
    }
}
