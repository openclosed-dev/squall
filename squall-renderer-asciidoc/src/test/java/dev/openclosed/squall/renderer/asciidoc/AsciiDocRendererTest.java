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
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Dialect;
import org.apache.commons.io.file.PathUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AsciiDocRendererTest {

    private static final Path BASE_DIR = Path.of("target", "test-runs");
    private static RendererFactory rendererFactory;
    private static ConfigLoader configLoader;

    public static void setUpBase(Dialect dialect) throws IOException {
        Locale.setDefault(Locale.ENGLISH);
        Files.createDirectories(BASE_DIR.resolve((dialect.dialectName())));
        rendererFactory = RendererFactory.get("pdf");
        configLoader = ConfigLoader.get();
    }

    protected void testRenderer(TestCase test, Dialect dialect) throws IOException {
        var builder = DatabaseSpec.builder();
        var parser = createParser(builder);

        for (var sql : test.sql()) {
            parser.parse(sql);
        }
        var spec = builder.build();

        var config = configLoader.loadRenderConfigFromJson(test.json());
        var renderer = rendererFactory.createRenderer(config);

        Path dir = prepareDirectory(dialect, test.title());

        renderer.render(spec, dir);

        String actual = readRenderedFile(dir);
        assertThat(convertRenderedContent(actual)).isEqualTo(test.expected());
    }

    protected static Stream<TestCase> loadTest(String name) {
        var filename = name + ".md";
        var in = AsciiDocRendererTest.class.getResourceAsStream(filename);
        return TestCase.loadFrom(in).stream();
    }

    protected abstract SqlParser createParser(DatabaseSpec.Builder builder);

    private static Path prepareDirectory(Dialect dialect, String title) throws IOException {
        Path parentDir = BASE_DIR.resolve(dialect.dialectName());
        Path dir = parentDir.resolve(title.replaceAll("\s", "-"));
        if (Files.exists(dir)) {
            PathUtils.cleanDirectory(dir);
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

    private static String convertRenderedContent(String content) {
        content = content.replaceAll("\\[\\w+\\]: .+", "");
        content = content.stripTrailing();
        return content;
    }
}
