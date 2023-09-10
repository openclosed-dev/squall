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
    private static RendererFactory rendererFactory;
    private static Map<Dialect, SqlParserFactory> parserFactories;

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
        rendererFactory = RendererFactory.get("html");
        parserFactories = new HashMap<>();
    }

    public static Stream<TestCase> postgresqlTests() {
        var dialect = MajorDialect.POSTGRESQL;
        return Stream.of(
            new TestCase("book", "book", "default", dialect)
            //new TestCase("book-ja", "book-ja", "japanese", dialect)
        );
    }

    @ParameterizedTest
    @MethodSource("postgresqlTests")
    public void testPostgresql(TestCase test) throws IOException {
        testRenderer(test);
    }

    private void testRenderer(TestCase test) throws IOException {
        var dialect = test.dialect();
        var builder = DatabaseSpec.builder();
        createParser(dialect, builder).parse(test.getSql());
        var spec = builder.build();

        var renderer = rendererFactory.createRenderer(test.getConfig());

        Path dir = prepareDirectory(dialect, test.title());
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

    private static Path prepareDirectory(Dialect dialect, String title) throws IOException {
        Path parentDir = BASE_DIR.resolve(dialect.dialectName());
        Path dir = parentDir.resolve(title);
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
