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

import java.util.Objects;
import java.util.ServiceLoader;

import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

/**
 * A factory of SQL parsers.
 */
public interface SqlParserFactory {

    Dialect dialect();

    default String dialectName() {
        return dialect().dialectName();
    }

    /**
     * Create a new parser.
     * @param config the configuration for the parser.
     * @param builder the builder of the database specification.
     * @return newly created parser.
     */
    default SqlParser createParser(ParserConfig config, DatabaseSpecBuilder builder) {
        return createParser(config, builder, CommentProcessor.empty());
    }

    /**
     * Create a new parser.
     * @param config the configuration for the parser.
     * @param builder the builder of the database specification.
     * @param commentProcessor the processor of comments.
     * @return newly created parser.
     */
    SqlParser createParser(
            ParserConfig config,
            DatabaseSpecBuilder builder,
            CommentProcessor commentProcessor);

    static SqlParserFactory get(Dialect dialect) {
        Objects.requireNonNull(dialect);
        return get(dialect.dialectName());
    }

    static SqlParserFactory get(String dialectName) {
        Objects.requireNonNull(dialectName);
        for (var factory : ServiceLoader.load(SqlParserFactory.class)) {
            if (factory.dialectName().equals(dialectName)) {
                return factory;
            }
        }
        throw new IllegalArgumentException();
    }
}
