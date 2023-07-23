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

package dev.openclosed.squall.core.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import dev.openclosed.squall.api.renderer.SequenceAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.config.ConfigurationException;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.Dialect;

final class DefaultConfigLoaderTest {

    private DefaultConfigLoader sut;

    @BeforeEach
    public void setUp() {
        this.sut = new DefaultConfigLoader();
    }

    @Test
    public void generateParserConfig() {
        Map<String, Object> map = Map.of(
                "dialect", "postgresql",
                "defaultSchema", "public"
                );

        var actual = sut.loadFromMap(map, ParserConfig.class);

        var expected = new ParserConfig(
                Dialect.POSTGRESQL.dialectName(),
                Optional.of("public"));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void generateParserConfigWithDefaultValues() {
        Map<String, Object> map = Collections.emptyMap();
        var actual = sut.loadFromMap(map, ParserConfig.class);
        assertThat(actual).isEqualTo(ParserConfig.getDefault());
    }

    @Test
    public void generateRenderConfig() {
        Map<String, Object> map = Map.of(
            "format", "markdown",
            "locale", "ja",
            "numbering", false,
            "order", "definition",
            "hide", Set.of("database", "schema"),
            "columnAttributes", List.of("name", "description"),
            "sequenceAttributes", List.of("data_type", "start")
        );

        var actual = sut.loadFromMap(map, RenderConfig.class);

        var expected = new RenderConfig(
            "markdown",
            "spec",
            Locale.JAPANESE,
            false,
            ComponentOrder.DEFINITION,
            Set.of(Component.Type.DATABASE, Component.Type.SCHEMA),
            List.of(ColumnAttribute.NAME, ColumnAttribute.DESCRIPTION),
            List.of(SequenceAttribute.DATA_TYPE, SequenceAttribute.START)
        );

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void generateRenderConfigWithDefaultValues() {
        Map<String, Object> map = Collections.emptyMap();
        var actual = sut.loadFromMap(map, RenderConfig.class);
        assertThat(actual).isEqualTo(RenderConfig.getDefault());
    }

    @Test
    public void generateRootConfig() {
        Map<String, Object> map = Map.of(
                "title", "this is a title",
                "sources", List.of(
                    "create-db.sql",
                    "create-schema.sql"
                ),
                "parser", Map.of(
                    "dialect", "postgresql",
                    "defaultSchema", "public"
                ),
                "renderers", Map.of(
                    "basic", Map.of(
                        "locale", "en",
                        "numbering", false,
                        "order", "name",
                        "hide", Set.of("database", "schema"),
                        "columnAttributes", List.of("name", "description"),
                        "sequenceAttributes", List.of("data_type", "start")
                        )
                    )
                );

        var actual = sut.loadFromMap(map, RootConfig.class);

        var expected = new RootConfig(
                Optional.of("this is a title"),
                List.of(
                    "create-db.sql",
                    "create-schema.sql"
                ),
                "output",
                new ParserConfig(
                        "postgresql",
                        Optional.of("public")),
                Map.of("basic", new RenderConfig(
                    "markdown",
                    "spec",
                    Locale.ENGLISH,
                    false,
                    ComponentOrder.NAME,
                    Set.of(Component.Type.DATABASE, Component.Type.SCHEMA),
                    List.of(ColumnAttribute.NAME, ColumnAttribute.DESCRIPTION),
                    List.of(SequenceAttribute.DATA_TYPE, SequenceAttribute.START)
                    )
                ));

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void generateRootConfigWithDefaultValues() {
        Map<String, Object> map = Collections.emptyMap();
        var actual = sut.loadFromMap(map, RootConfig.class);
        assertThat(actual).isEqualTo(new RootConfig());
    }

    public static Stream<ConfigTestCase> invalidJson() {
        return ConfigTestCase.loadFrom("invalid-json.md").stream();
    }

    @ParameterizedTest
    @MethodSource("invalidJson")
    public void shouldFailIfJsonIsInvalid(ConfigTestCase testCase) {
        var thrown = catchThrowable(() -> {
            sut.loadFromJson(testCase.json());
        });
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
        var thrown = catchThrowable(() -> {
            sut.loadFromJson(testCase.json());
        });
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
