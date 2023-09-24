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

package dev.openclosed.squall.cli.command.config;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import dev.openclosed.squall.api.renderer.PageOrientation;
import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Component;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.cli.command.BaseTestCase;

public final class InitTest {

    private static ConfigLoader configLoader;

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
        configLoader = ConfigLoader.get();
    }

    record TestCase(String name, int exitCode, String... args) implements BaseTestCase {

        private static final List<String> SUBCOMMAND = List.of("config", "init");

        @Override
        public List<String> subcommand() {
            return SUBCOMMAND;
        }

        @Override
        public String toString() {
            return name();
        }
    }

    public static Stream<TestCase> testCases() {
        return Stream.of(
                new TestCase("empty", 0),
                new TestCase("already-exists", 1),
                new TestCase("help", 0, "--help")
                );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testCommand(TestCase testCase) throws IOException {
        int exitCode = testCase.run().exitCode();
        var file = testCase.directory().resolve("squall.json");

        assertThat(exitCode).isEqualTo(testCase.exitCode());

        if (testCase.name().equals("help")) {
            return;
        }

        assertThat(Files.exists(file)).isTrue();
        if (exitCode == 0) {
            var json = Files.readString(file);
            var config = configLoader.loadFromJson(json);
            verifyConfig(config);
        }
    }

    private static void verifyConfig(RootConfig root) {
        var metadata = root.metadata();
        assertThat(metadata).isNotEmpty();

        assertThat(root.sources()).isEmpty();
        assertThat(root.outDir()).isEqualTo("output");

        ParserConfig parser = root.parser();
        assertThat(parser.dialect()).isEqualTo("postgresql");

        assertThat(root.renderers().keySet()).containsExactly("html", "pdf", "markdown");
        verifyRenderConfig(root.renderers().get("html"), "html");
        verifyPdfRenderConfig(root.renderers().get("pdf"), "pdf");
        verifyRenderConfig(root.renderers().get("markdown"), "markdown");
    }

    private static void verifyRenderConfig(RenderConfig config, String format) {
        assertThat(config.format()).isEqualTo(format);
        assertThat(config.basename()).isEqualTo("spec");
        assertThat(config.locale()).isEqualTo(Locale.ENGLISH);
        assertThat(config.numbering()).isTrue();
        assertThat(config.order()).isEqualTo(ComponentOrder.NAME);
        assertThat(config.show()).isEqualTo(Component.Type.all());
        assertThat(config.columnAttributes()).isEqualTo(ColumnAttribute.defaultList());
        assertThat(config.sequenceAttributes()).isEqualTo(SequenceAttribute.defaultList());
    }

    private static void verifyPdfRenderConfig(RenderConfig config, String format) {
        verifyRenderConfig(config, format);
        assertThat(config.pageSize()).isEqualTo("a4");
        assertThat(config.pageOrientation()).isEqualTo(PageOrientation.LANDSCAPE);
    }
}
