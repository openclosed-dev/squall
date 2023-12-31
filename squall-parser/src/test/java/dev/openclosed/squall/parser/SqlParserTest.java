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

package dev.openclosed.squall.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static dev.openclosed.squall.api.util.Records.toMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import dev.openclosed.squall.api.util.Records;
import dev.openclosed.squall.api.parser.SqlSyntaxException;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.spec.Table;
import dev.openclosed.squall.api.text.json.JsonWriter;
import org.junit.jupiter.api.BeforeEach;

import dev.openclosed.squall.api.text.Problem;
import dev.openclosed.squall.api.parser.SqlParser;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public abstract class SqlParserTest {

    private static final Path BASE_OUTPUT_DIR = Path.of("target", "test-output");

    private DatabaseSpec.Builder builder;
    private SqlParser sqlParser;

    private static JsonWriter jsonWriter;

    @BeforeEach
    public final void setUp() {
        this.builder = DatabaseSpec.builder();
        this.sqlParser = createParser(this.builder);
    }

    @ParameterizedTest
    @MethodSource
    public void parseBasic(SqlTestCase test) {
        testParser(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseDatabases(SqlTestCase test) {
        testParser(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseSchemas(SqlTestCase test) {
        testParser(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseTables(SqlTestCase test) {
        testTable(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseColumn(SqlTestCase test) {
        testColumn(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseSequence(SqlTestCase test) {
        testSequence(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseDataTypes(SqlTestCase test) {
        testDataType(test);
    }

    @ParameterizedTest
    @MethodSource
    public void parseExpression(SqlTestCase test) {
        SqlParser parser = getParser();
        Expression expression = parser.parseExpression(test.firstSql());
        assertThat(toMap(expression)).isEqualTo((test.jsonAsMap()));
        assertThat(expression.toSql()).isEqualTo(test.text());
    }

    @ParameterizedTest
    @MethodSource
    public void parseInvalidExpression(SqlTestCase test) {
        SqlParser parser = getParser();
        Throwable thrown = catchThrowable(() -> {
            parser.parseExpression(test.firstSql());
        });
        assertThat(thrown).isInstanceOf(SqlSyntaxException.class);
        if (thrown instanceof SqlSyntaxException e) {
            System.out.println(e.getBundledMessage().get());
        }
    }

    @ParameterizedTest
    @MethodSource
    public void parseSourceWithErrors(SqlTestCase test) {
        SqlParser parser = getParser();
        int errors = parser.parse(test.firstSql());
        assertThat(errors).isGreaterThan(0);
        var problems = parser.getProblems();
        assertThat(problems).isNotEmpty();
        var output = handleProblems(problems);
        assertThat(output).isEqualTo(test.output());
    }

    @ParameterizedTest
    @MethodSource
    public void parseSqlFilesWithoutErrors(String fileName) {
        String sql = loadSqlFromResource(fileName);
        SqlParser parser = getParser();
        int errors = parser.parse(sql);
        if (errors > 0) {
            handleProblems(parser.getProblems());
        }
        assertThat(errors).isEqualTo(0);

        DatabaseSpec spec = builder.build();
        saveSpecAsJson(fileName.replace(".sql", ".json"), spec);
    }

    protected abstract SqlParser createParser(DatabaseSpec.Builder builder);

    protected final SqlParser getParser() {
        return sqlParser;
    }

    protected static Stream<SqlTestCase> loadTests(String... names) {
        return Stream.of(names)
            .flatMap(name -> loadTests(name, SqlParserTest.class));
    }

    protected static Stream<SqlTestCase> loadTests(String name, Class<?> clazz) {
        return SqlTestCase.loadFrom(name, clazz).stream();
    }

    protected static String loadSqlFromResource(String name) {
        try (InputStream in = SqlParserTest.class.getResourceAsStream(name)) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void testParser(SqlTestCase test) {
        for (var sql : test.sql()) {
            sqlParser.parse(sql);
        }
        assertThat(sqlParser.getProblems()).isEmpty();
        var spec = builder.build();
        assertThat(toMap(spec)).isEqualTo(test.jsonAsMap());
    }

    protected final void testTable(SqlTestCase test) {
        sqlParser.parse(test.firstSql());
        var spec = builder.build();
        List<Table> tables = spec
                .databases().iterator().next()
                .schemas().iterator().next()
                .tables();

        if (tables.size() > 1) {
            var maps = tables.stream().map(Records::toMap).toList();
            assertThat(maps).isEqualTo(test.jsonAsMaps());
        } else {
            Table table = tables.iterator().next();
            assertThat(toMap(table)).isEqualTo(test.jsonAsMap());
        }
    }

    protected final void testColumn(SqlTestCase test) {
        sqlParser.parse(test.firstSql());
        var spec = builder.build();
        var column = spec
                .databases().iterator().next()
                .schemas().iterator().next()
                .tables().iterator().next()
                .columns().iterator().next();

        assertThat(toMap(column)).isEqualTo(test.jsonAsMap());
    }

    protected final void testSequence(SqlTestCase test) {
        sqlParser.parse(test.firstSql());
        var spec = builder.build();
        var sequence = spec
            .databases().iterator().next()
            .schemas().iterator().next()
            .sequences().iterator().next();

        assertThat(toMap(sequence)).isEqualTo(test.jsonAsMap());
    }

    protected final void testDataType(SqlTestCase test) {
        sqlParser.parse(test.firstSql());
        var spec = builder.build();
        var column = spec
                .databases().iterator().next()
                .schemas().iterator().next()
                .tables().iterator().next()
                .columns().iterator().next();

        var actual = toMap(column);

        assertThat(actual).isEqualTo(test.jsonAsMap());
    }

    protected static String handleProblems(List<Problem> problems) {
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

    private static JsonWriter getJsonWriter() {
        if (jsonWriter == null) {
            jsonWriter = JsonWriter.newInstance();
        }
        return jsonWriter;
    }

    protected static void saveSpecAsJson(String fileName, DatabaseSpec spec) {
        try {
            Path path = BASE_OUTPUT_DIR.resolve(fileName);
            Files.createDirectories(path.getParent());
            String json = getJsonWriter().writeObject(toMap(spec));
            Files.writeString(path, json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
