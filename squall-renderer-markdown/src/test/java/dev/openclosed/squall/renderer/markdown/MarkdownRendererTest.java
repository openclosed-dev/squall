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

package dev.openclosed.squall.renderer.markdown;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

import dev.openclosed.squall.api.spec.MajorDialect;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.parser.CommentHandlers;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

final class MarkdownRendererTest {

    private static SqlParserFactory parserFactory;
    private static RendererFactory rendererFactory;
    private static ConfigLoader configLoader;

    private static final Path BASE_DIR = Path.of("target", "test-runs");

    @BeforeAll
    public static void setUpOnce() throws IOException {
        Locale.setDefault(Locale.ENGLISH);
        Files.createDirectories(BASE_DIR);
        parserFactory = SqlParserFactory.get(MajorDialect.POSTGRESQL);
        rendererFactory = RendererFactory.get("markdown");
        configLoader = ConfigLoader.get();
    }

    public static Stream<RenderTest> testRenderer() {
        return Stream.of(
            "database",
            "schema",
            "table",
            "hide-database-and-schema",
            "sequence"
        )
        .flatMap(MarkdownRendererTest::loadTest);
    }

    @ParameterizedTest
    @MethodSource
    public void testRenderer(RenderTest test) throws IOException {
        var builder = DatabaseSpecBuilder.newBuilder();
        var parser = parserFactory.createParser(
                ParserConfig.getDefault(),
                builder,
                CommentHandlers.createDocCommentHandler());

        for (var sql : test.sql()) {
            parser.parse(sql);
        }
        var spec = builder.build();

        var config = configLoader.loadRenderConfigFromJson(test.json());
        var renderer = rendererFactory.createRenderer(config);

        Path dir = prepareDirectory(test.title());

        renderer.render(spec, dir);

        String actual = readRenderedFile(dir);
        assertThat(convertRenderedContent(actual)).isEqualTo(test.expected());
    }

    private static Stream<RenderTest> loadTest(String name) {
        var filename = name + ".md";
        var in = MarkdownRendererTest.class.getResourceAsStream(filename);
        return RenderTest.loadFrom(in).stream();
    }

    private static Path prepareDirectory(String title) throws IOException {
        Path dir = BASE_DIR.resolve(title.replaceAll("\s", "-"));
        if (Files.exists(dir)) {
            PathUtils.cleanDirectory(dir);
        }
        return dir;
    }

    private static String readRenderedFile(Path dir) {
        Path path = dir.resolve("spec.md");
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String convertRenderedContent(String content) {
        content = content.replaceAll("\\[\\w+\\]: .+", "");
        content = content.stripTrailing();
        return content;
    }
}
