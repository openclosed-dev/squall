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

import java.util.Locale;
import java.util.Objects;
import java.util.ServiceLoader;

import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.Dialect;

/**
 * Factory of SQL parsers.
 */
public interface SqlParserFactory {

    /**
     * Returns the SQL dialect for which this factory creates parsers.
     * @return the SQL dialect.
     */
    Dialect dialect();

    /**
     * Returns the name of the SQL dialect.
     * @return the name of the SQL dialect.
     */
    default String dialectName() {
        return dialect().name();
    }

    /**
     * Create a new SQL parser.
     * @param config the configuration for the parser.
     * @param builder the builder of the database specification.
     * @return newly created parser.
     */
    default SqlParser createParser(ParserConfig config, DatabaseSpec.Builder builder) {
        return createParser(config, builder, CommentProcessor.noop());
    }

    /**
     * Create a new SQL parser with specified comment processor.
     * @param config the configuration for the parser.
     * @param builder the builder of the database specification.
     * @param commentProcessor the processor of comments.
     * @return newly created parser.
     */
    default SqlParser createParser(
            ParserConfig config,
            DatabaseSpec.Builder builder,
            CommentProcessor commentProcessor) {
        return createParser(
            config, builder, commentProcessor, MessageBundle.forLocale(Locale.getDefault()));
    }

    /**
     * Create a new SQL parser with specified comment processor and message bundle.
     * @param config the configuration for the parser.
     * @param builder the builder of the database specification.
     * @param commentProcessor the processor of comments.
     * @param messageBundle the message bundle.
     * @return newly created parser.
     */
    SqlParser createParser(
        ParserConfig config,
        DatabaseSpec.Builder builder,
        CommentProcessor commentProcessor,
        MessageBundle messageBundle);

    /**
     * Creates a parser factory for the SQL dialect.
     * @param dialect the SQL dialect for which the factory creates parsers.
     * @return created parser factory.
     * @throws IllegalArgumentException if appropriate factory was not found.
     */
    static SqlParserFactory newFactory(Dialect dialect) {
        Objects.requireNonNull(dialect);
        return newFactory(dialect.name());
    }

    /**
     * Creates a parser factory for the SQL dialect.
     * @param dialectName the name of the SQL dialect for which the factory creates parsers.
     * @return created parser factory.
     * @throws IllegalArgumentException if appropriate factory was not found for the dialect.
     */
    static SqlParserFactory newFactory(String dialectName) {
        Objects.requireNonNull(dialectName);
        for (var factory : ServiceLoader.load(SqlParserFactory.class)) {
            if (factory.dialectName().equals(dialectName)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("SqlParserFactory was not found for the dialect: " + dialectName);
    }
}
