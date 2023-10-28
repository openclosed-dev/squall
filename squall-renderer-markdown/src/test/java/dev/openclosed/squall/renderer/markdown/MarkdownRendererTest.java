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

package dev.openclosed.squall.renderer.markdown;

import static org.assertj.core.api.Assertions.*;

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.doc.DocCommentProcessor;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class MarkdownRendererTest {

    static final Path BASE_DIR = Path.of("target", "test-runs");

    private static ConfigLoader configLoader;

    private RendererFactory renderFactory;

    @BeforeAll
    public static void setUpOnce() {
        configLoader = ConfigLoader.newLoader();
    }

    @BeforeEach
    public void setUp() {
        renderFactory = new MarkdownRendererFactory();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "column-description-blank-line",
        "column-description-multiple-lines",
        "database",
        "database-with-comment",
        "deprecated-column",
        "deprecated-table",
        "foreign-key",
        "foreign-key-in-qualified-table",
        "hide-database-and-schema",
        "schema",
        "sequence-qualified",
        "schema-with-comment",
        "table-in-default-schema",
        "table-qualified"
    })
    public void testPostgresql(String title) throws IOException {
        testWithDialect(title, Dialect.POSTGRESQL);
    }

    void testWithDialect(String title, Dialect dialect) throws IOException {
        var spec = parseSql(title, dialect);
        var config = loadRenderConfig(dialect, title);
        var renderer = renderFactory.createRenderer(config, config.locale());
        var dir = prepareDirectory(dialect, title);
        renderer.render(spec, dir);

        var path = dir.resolve("spec.md");
        assertThat(Files.exists(path)).isTrue();
        var expected = readTextResource(dialect.name(), title, "expected.md");
        assertThat(readRenderedText(path)).isEqualTo(expected);
    }

    static DatabaseSpec parseSql(String title, Dialect dialect) {
        var builder = DatabaseSpec.builder();
        var parser = createParser(dialect, builder);
        String sql = readTextResource(dialect.name(), title, "input.sql");
        parser.parse(sql);
        return builder.build();
    }

    static SqlParser createParser(Dialect dialect, DatabaseSpec.Builder builder) {
        var config = new ParserConfig(dialect.name(), dialect.defaultSchema());
        return SqlParserFactory.newFactory(dialect).createParser(
            config,
            builder,
            new DocCommentProcessor());
    }

    static RenderConfig loadRenderConfig(Dialect dialect, String title) {
        String text = findTextResource(dialect, title, "config.json");
        return configLoader.loadRenderConfigFromJson(text);
    }

    static String findTextResource(Dialect dialect, String title, String name) {
        return findTextResource(dialect.name(), title, name);
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
        var in = MarkdownRendererTest.class.getResourceAsStream(name);
        if (in == null) {
            return null;
        }
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Path prepareDirectory(Dialect dialect, String title) throws IOException {
        Path parentDir = BASE_DIR.resolve(dialect.name());
        Path dir = parentDir.resolve(title.replaceAll("\\s", "-"));
        if (Files.exists(dir)) {
            PathUtils.cleanDirectory(dir);
        }
        return dir;
    }

    static String readRenderedText(Path path) throws IOException {
        String text = Files.readString(path);
        return convertRenderedContent(text);
    }

    private static String convertRenderedContent(String content) {
        content = content.replaceAll("\\[\\w+]: .+", "");
        content = content.stripTrailing();
        return content;
    }
}
