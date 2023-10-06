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

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.api.sql.spec.MajorDialect;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;
import dev.openclosed.squall.doc.DocCommentProcessor;
import dev.openclosed.squall.renderer.asciidoc.html.HtmlRendererFactory;
import dev.openclosed.squall.renderer.asciidoc.pdf.PdfRendererFactory;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AsciiDocRendererTest {

    private static final Path BASE_DIR = Path.of("target", "test-runs");

    private static ConfigLoader configLoader;

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
        configLoader = ConfigLoader.get();
    }

    public static Stream<String> postgresqlTests() {
        return Stream.of(
            "book",
            "book-ja"
        );
    }

    @ParameterizedTest
    @MethodSource("postgresqlTests")
    public void testHtmlRendererForPostgresql(String title) throws IOException {
        Path outputDir = testRenderer(title, MajorDialect.POSTGRESQL, "html");
        Path outputFile = outputDir.resolve("spec.html");
        assertThat(Files.exists(outputFile)).isTrue();
        assertThat(Files.readString(outputFile))
            .startsWith("<!DOCTYPE html>")
            .endsWith("</html>");
    }

    @EnabledIfSystemProperty(named = "test.full", matches = ".*")
    @ParameterizedTest()
    @MethodSource("postgresqlTests")
    public void testPdfRendererForPostgresql(String title) throws IOException {
        Path outputDir = testRenderer(title, MajorDialect.POSTGRESQL, "pdf");
        Path outputFile = outputDir.resolve("spec.pdf");
        assertThat(Files.exists(outputFile)).isTrue();
    }

    Path testRenderer(String title, Dialect dialect, String format) throws IOException {
        var spec = parseSql(title, dialect);

        var rendererFactory = switch (format) {
            case "html" -> new HtmlRendererFactory();
            case "pdf" -> new PdfRendererFactory();
            default -> throw new IllegalArgumentException(format);
        };
        var config = loadRenderConfig(dialect, title);
        var renderer = rendererFactory.createRenderer(config, config.locale());
        var dir = prepareDirectory(dialect, title, format);
        renderer.render(spec, dir);

        return dir;
    }

    static DatabaseSpec parseSql(String title, Dialect dialect) {
        var builder = DatabaseSpec.builder();
        var metadata = loadMetadata(dialect, title);
        builder.setMetadata(metadata);
        var parser = createParser(dialect, builder);
        String sql = readTextResource(dialect.dialectName(), title, "input.sql");
        parser.parse(sql);
        return builder.build();
    }

    static SqlParser createParser(Dialect dialect, DatabaseSpec.Builder builder) {
        var config = new ParserConfig(dialect.dialectName(), dialect.defaultSchema());
        return SqlParserFactory.get(dialect).createParser(
            config,
            builder,
            new DocCommentProcessor());
    }

    static RenderConfig loadRenderConfig(Dialect dialect, String title) {
        String text = findTextResource(dialect, title, "config.json");
        return configLoader.loadRenderConfigFromJson(text);
    }

    static SpecMetadata loadMetadata(Dialect dialect, String title) {
        String text = findTextResource(dialect, title, "metadata.json");
        return configLoader.loadMetadataFromJson(text);
    }

    static String findTextResource(Dialect dialect, String title, String name) {
        return findTextResource(dialect.dialectName(), title, name);
    }

    static String findTextResource(String dialect, String title, String name) {
        String text = readTextResource(dialect, title, name);
        if (text == null) {
            text = readTextResource(dialect, name);
            if (text == null) {
                throw new IllegalStateException();
            }
        }
        return text;
    }

    static String readTextResource(String... names) {
        return readTextResource(String.join("/", names));
    }

    static String readTextResource(String name) {
        var in = AsciiDocRendererTest.class.getResourceAsStream(name);
        if (in == null) {
            return null;
        }
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Path prepareDirectory(Dialect dialect, String title, String format) throws IOException {
        Path parentDir = BASE_DIR.resolve(dialect.dialectName());
        Path dir = parentDir.resolve(title).resolve(format);
        if (Files.exists(dir)) {
            PathUtils.cleanDirectory(dir);
        }
        return dir;
    }
}
