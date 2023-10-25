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

package dev.openclosed.squall.api.test.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.config.ConfigurationException;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.renderer.PageOrientation;
import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.Component;
import dev.openclosed.squall.api.sql.spec.ComponentOrder;

public final class DefaultConfigLoaderTest {

    private ConfigLoader sut;

    @BeforeEach
    public void setUp() {
        this.sut = ConfigLoader.newLoader();
    }

    @Test
    public void shouldLoadSpecMetadataFromJson() {

        String text = """
            {
                "title": "title1",
                "author": "author1",
                "version": "1.0.0",
                "date": "2023-01-01"
            }
            """;

        var expected = new SpecMetadata(
            "title1",
            Optional.of("author1"),
            Optional.of("1.0.0"),
            Optional.of("2023-01-01")
        );

        SpecMetadata actual = sut.loadMetadataFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldLoadDefaultSpecMetadataFromEmptyJson() {
        String text = "{}";
        var expected = SpecMetadata.DEFAULT;
        var actual = sut.loadMetadataFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldLoadRenderConfigFromJson() {

        String text = """
            {
                "format": "html",
                "baseName": "spec",
                "locale": "en",
                "numbering": true,
                "order": "definition",
                "show": ["database", "schema"],
                "columnAttributes": ["name", "description"],
                "sequenceAttributes": ["type_name", "start"],
                "pageSize" : "a4",
                "pageOrientation": "portrait",
                "pageMargin": ["5mm", "10mm", "15mm", "20mm"]
            }
            """;

        var expected = new RenderConfig(
            "html",
            "spec",
            Locale.ENGLISH,
            true,
            ComponentOrder.DEFINITION,
            Set.of(Component.Type.DATABASE, Component.Type.SCHEMA),
            List.of(ColumnAttribute.NAME, ColumnAttribute.DESCRIPTION),
            List.of(SequenceAttribute.TYPE_NAME, SequenceAttribute.START),
            "a4",
            PageOrientation.PORTRAIT,
            List.of("5mm", "10mm", "15mm", "20mm")
        );

        var actual = sut.loadRenderConfigFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldLoadDefaultRenderConfigFromEmptyJson() {
        String text = "{}";
        var expected = RenderConfig.getDefault();
        var actual = sut.loadRenderConfigFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldLoadRootConfigFromJson() {

        String text = """
            {
                "metadata": {
                    "title": "title1",
                    "author": "author1",
                    "version": "1.0.0",
                    "date": "2023-01-01"
                },
                "sources": [
                    "create-db.sql",
                    "create-schema.sql"
                ],
                "outDir": "output",
                "parser": {
                    "dialect": "postgresql",
                    "defaultSchema": "public"
                },
                "renderers": {
                    "default": {
                        "format": "html",
                        "baseName": "spec",
                        "locale": "en",
                        "numbering": true,
                        "order": "definition",
                        "show": ["database", "schema"],
                        "columnAttributes": ["name", "description"],
                        "sequenceAttributes": ["type_name", "start"],
                        "pageSize" : "a4",
                        "pageOrientation": "portrait",
                        "pageMargin": ["5mm", "10mm", "15mm", "20mm"]
                    }
                }
            }
            """;

        var expected = new RootConfig(
            Optional.of(new SpecMetadata(
                "title1",
                Optional.of("author1"),
                Optional.of("1.0.0"),
                Optional.of("2023-01-01")
            )),
            List.of(
                "create-db.sql",
                "create-schema.sql"
            ),
            "output",
            new ParserConfig(
                Dialect.POSTGRESQL.name(),
                "public"
            ),
            Map.of("default", new RenderConfig(
                "html",
                "spec",
                Locale.ENGLISH,
                true,
                ComponentOrder.DEFINITION,
                Set.of(Component.Type.DATABASE, Component.Type.SCHEMA),
                List.of(ColumnAttribute.NAME, ColumnAttribute.DESCRIPTION),
                List.of(SequenceAttribute.TYPE_NAME, SequenceAttribute.START),
                "a4",
                PageOrientation.PORTRAIT,
                List.of("5mm", "10mm", "15mm", "20mm")
            ))
        );

        var actual = sut.loadFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldLoadDefaultRootConfigFromEmptyJson() {
        String text = "{}";
        var expected = new RootConfig();
        var actual = sut.loadFromJson(text);
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<ConfigTestCase> invalidJson() {
        return ConfigTestCase.loadFrom("invalid-json.md").stream();
    }

    @ParameterizedTest
    @MethodSource("invalidJson")
    public void shouldFailIfJsonIsInvalid(ConfigTestCase testCase) {
        var thrown = catchThrowable(() -> sut.loadFromJson(testCase.json()));
        assertThat(thrown).isInstanceOf(ConfigurationException.class);
        var output = handleProblems(sut.getProblems());
        assertThat(output).isEqualTo(testCase.output());
    }

    public static Stream<ConfigTestCase> validConfigurations() {
        return ConfigTestCase.loadFrom("valid-configs.md").stream();
    }

    @ParameterizedTest
    @MethodSource("validConfigurations")
    public void shouldLoadValidConfig(ConfigTestCase testCase) {
        var config = sut.loadFromJson(testCase.json());
        assertThat(config).isNotNull();
        var output = handleProblems(sut.getProblems());
        assertThat(output).isEqualTo(testCase.output());
    }

    public static Stream<ConfigTestCase> invalidConfigurations() {
        return ConfigTestCase.loadFrom("invalid-configs.md").stream();
    }

    @ParameterizedTest
    @MethodSource("invalidConfigurations")
    public void shouldFailIfConfigurationIsInvalid(ConfigTestCase testCase) {
        var thrown = catchThrowable(() -> sut.loadFromJson(testCase.json()));
        assertThat(thrown).isInstanceOf(ConfigurationException.class);
        var output = handleProblems(sut.getProblems());
        assertThat(output).isEqualTo(testCase.output());
    }

    private static String handleProblems(List<Problem> problems) {
        var builder = new StringBuilder();
        for (var problem : problems) {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append(problem.severity()).append(": ");
            builder.append(problem);
        }
        var output = builder.toString();
        System.out.println(output);
        return output;
    }
}
