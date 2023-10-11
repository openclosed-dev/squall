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

package dev.openclosed.squall.renderer.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.doc.DocCommentProcessor;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.RendererFactory;

public final class JsonRendererTest {

    private static SqlParserFactory parserFactory;
    private static RendererFactory rendererFactory;

    private static final Path BASE_DIR = Path.of("target", "test-runs");

    @BeforeAll
    public static void setUpOnce() {
        parserFactory = SqlParserFactory.get(Dialect.POSTGRESQL);
        rendererFactory = RendererFactory.get("json");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "sample",
        "empty"
    })
    public void testRenderer(String name) throws IOException {
        var spec = parseSpec(name + ".sql");

        Path dir = prepareDirectory(name);

        var renderer = rendererFactory.createRenderer(RenderConfig.getDefault());
        renderer.render(spec, dir);
    }

    private DatabaseSpec parseSpec(String name) throws IOException {
        var sql = readResource(name);
        var builder = DatabaseSpec.builder();
        var parser = parserFactory.createParser(
            ParserConfig.DEFAULT,
            builder,
            new DocCommentProcessor());
        parser.parse(sql);
        return builder.build();
    }

    private String readResource(String name) throws IOException {
        var in = getClass().getResourceAsStream(name);
        try (var reader = new BufferedReader(
            new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private Path prepareDirectory(String name) throws IOException {
        Path dir = BASE_DIR.resolve(name);
        Files.createDirectories(dir);
        PathUtils.cleanDirectory(dir);
        return dir;
    }
}
