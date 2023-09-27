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

package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.MajorDialect;
import dev.openclosed.squall.doc.DocCommentProcessor;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AsciiDocRendererTest {

    private static final Path BASE_DIR = Path.of("target", "test-runs");
    private static Map<Dialect, SqlParserFactory> parserFactories;

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
        parserFactories = new HashMap<>();
    }

    public static Stream<TestCase> testsForPostgresql() {
        var dialect = MajorDialect.POSTGRESQL;
        return Stream.of(
            new TestCase(
                "book", "book", "config", "metadata-book", dialect),
            new TestCase(
                "book-ja", "book-ja", "config-ja", "metadata-book-ja", dialect)
        );
    }

    @ParameterizedTest
    @MethodSource("testsForPostgresql")
    public void testHtmlForPostgresql(TestCase test) throws IOException {
        testRenderer(test, "html");
    }

    @EnabledIfSystemProperty(named = "test.full", matches = ".*")
    @ParameterizedTest
    @MethodSource("testsForPostgresql")
    public void testPdfForPostgresql(TestCase test) throws IOException {
        testRenderer(test, "pdf");
    }

    private void testRenderer(TestCase test, String format) throws IOException {
        var dialect = test.dialect();
        var builder = DatabaseSpec.builder();
        builder.setMetadata(test.getMetadata());
        createParser(dialect, builder).parse(test.getSql());
        var spec = builder.build();

        var rendererFactory = RendererFactory.get(format);
        var renderer = rendererFactory.createRenderer(test.getConfig());

        Path dir = prepareDirectory(dialect, test.title(), format);
        renderer.render(spec, dir);

        test.getExpectedText().ifPresent(expected -> {
            var actual = readRenderedFile(dir);
            assertThat(actual).isEqualTo(expected);
        });
    }

    private SqlParser createParser(Dialect dialect, DatabaseSpec.Builder builder) {
        return getParserFactoryFor(dialect).createParser(
            ParserConfig.getDefault(),
            builder,
            new DocCommentProcessor());
    }

    private SqlParserFactory getParserFactoryFor(Dialect dialect) {
        if (!parserFactories.containsKey(dialect)) {
            parserFactories.put(dialect, SqlParserFactory.get(dialect));
        }
        return parserFactories.get(dialect);
    }

    private static Path prepareDirectory(Dialect dialect, String title, String format) throws IOException {
        Path dir = BASE_DIR.resolve(dialect.dialectName()).resolve(title).resolve(format);
        if (Files.exists(dir)) {
            PathUtils.cleanDirectory(dir);
        } else {
            Files.createDirectories(dir);
        }
        return dir;
    }

    private static String readRenderedFile(Path dir) {
        Path path = dir.resolve("spec.adoc");
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
