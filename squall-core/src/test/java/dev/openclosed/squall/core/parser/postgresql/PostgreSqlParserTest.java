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

import java.util.stream.Stream;

import dev.openclosed.squall.api.spec.MajorDialect;
import dev.openclosed.squall.core.parser.SqlParserTest;
import dev.openclosed.squall.core.parser.SqlTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.parser.CommentHandlers;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

public final class PostgreSqlParserTest extends SqlParserTest {

    private static final PostgreSqlParserFactory PARSER_FACTORY = new PostgreSqlParserFactory();
    private static final ParserConfig PARSER_CONFIG = new ParserConfig(
        MajorDialect.POSTGRESQL.dialectName(), ""
    );

    @Override
    protected SqlParser createParser(DatabaseSpecBuilder builder) {
        return PARSER_FACTORY.createParser(
            PARSER_CONFIG,
            builder,
            CommentHandlers.createDocCommentHandler());
    }

    // Additional tests

    public static Stream<SqlTestCase> parseDataTypesForPostgreSql() {
        return loadTests("postgresql/data-type-postgres.md");
    }

    @ParameterizedTest
    @MethodSource
    public void parseDataTypesForPostgreSql(SqlTestCase test) {
        testDataType(test);
    }

    public static Stream<SqlTestCase> parseBasic() {
        return loadTests(
            "basic.md",
            "postgresql/metacommand.md");
    }

    public static Stream<SqlTestCase> parseSourceWithErrors() {
        return loadTests("basic-error.md");
    }

    public static Stream<SqlTestCase> parseDatabases() {
        return loadTests("database.md");
    }

    public static Stream<SqlTestCase> parseSchemas() {
        return loadTests("schema.md");
    }

    public static Stream<SqlTestCase> parseSequence() {
        return loadTests("sequence.md");
    }

    public static Stream<SqlTestCase> parseTables() {
        return loadTests("table.md");
    }

    public static Stream<SqlTestCase> parseColumn() {
        return loadTests("column.md");
    }

    public static Stream<SqlTestCase> parseDataTypes() {
        return loadTests("data-type.md");
    }

    public static Stream<SqlTestCase> parseExpression() {
        return loadTests(
            "expression.md",
            "predicate.md",
            "postgresql/predicate.md"
        );
    }

    public static Stream<SqlTestCase> parseInvalidExpression() {
        return loadTests("expression-error.md");
    }

    public static Stream<String> parseSqlFilesWithoutErrors() {
        return Stream.of(
          "postgresql/redmine-schema.sql"
        );
    }

    public static Stream<SqlTestCase> parseDocComment() {
        return loadTests("doc-comment.md");
    }

    public static Stream<SqlTestCase> parseDocCommentWithError() {
        return loadTests("doc-comment-error.md");
    }
}
