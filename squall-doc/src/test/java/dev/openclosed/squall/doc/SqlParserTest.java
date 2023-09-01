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

package dev.openclosed.squall.doc;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.MajorDialect;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class SqlParserTest {

    private static final Dialect DIALECT = MajorDialect.POSTGRESQL;
    private static SqlParserFactory parserFactory;
    private static final ParserConfig PARSER_CONFIG = new ParserConfig(
        DIALECT.dialectName(), ""
    );

    private DatabaseSpecBuilder builder;
    private SqlParser sqlParser;

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = SqlParserFactory.get(DIALECT);
    }

    @BeforeEach
    public void setUp() {
        this.builder = DatabaseSpecBuilder.newBuilder();
        this.sqlParser = createParser(this.builder);
    }

    public static Stream<SqlTestCase> normalTestCases() {
        return loadTests("annotated-sql.md");
    }

    public static Stream<SqlTestCase> errorTestCases() {
        return loadTests("annotated-sql-error.md");
    }

    @ParameterizedTest
    @MethodSource("normalTestCases")
    public void parseAnnotatedSql(SqlTestCase test) {
        SqlParser parser = getParser();
        int errors = parser.parse(test.firstSql());
        if (errors > 0) {
            handleProblems(parser.getProblems());
        }
        assertThat(errors).isEqualTo(0);
        var spec = builder.build();
        assertThat(spec.toMap()).isEqualTo(test.jsonAsMap());
    }

    @ParameterizedTest
    @MethodSource("errorTestCases")
    public void parseAnnotatedSqlWithError(SqlTestCase test) {
        SqlParser parser = getParser();
        int errors = parser.parse(test.firstSql());
        assertThat(errors).isGreaterThan(0);

        var spec = builder.build();
        assertThat(spec.toMap()).isEqualTo(test.jsonAsMap());

        var output = handleProblems(parser.getProblems());
        assertThat(output).isEqualTo(test.output());
    }

    SqlParser getParser() {
        return sqlParser;
    }

    SqlParser createParser(DatabaseSpecBuilder builder) {
        return parserFactory.createParser(
            PARSER_CONFIG,
            builder,
            new DocCommentProcessor());
    }

    static Stream<SqlTestCase> loadTests(String... names) {
        return Stream.of(names)
            .flatMap(name -> loadTests(name, SqlParserTest.class));
    }

    static Stream<SqlTestCase> loadTests(String name, Class<?> clazz) {
        return SqlTestCase.loadFrom(name, clazz).stream();
    }

    static String handleProblems(List<Problem> problems) {
        var builder = new StringBuilder();
        for (var problem : problems) {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append(problem.severity()).append(": ");
            builder.append(problem.toString());
        }
        var output = builder.toString();
        System.out.println(output);
        return output;
    }
}
