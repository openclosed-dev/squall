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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import dev.openclosed.squall.api.renderer.PageOrientation;
import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.MajorDialect;
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

    private static final RootConfig DEFAULT_CONFIG =
            new RootConfig(
                    Optional.of("Untitled"),
                    List.of(),
                    "output",
                    new ParserConfig(MajorDialect.POSTGRESQL),
                    Map.of(
                        "markdown", new RenderConfig(
                            "markdown",
                            "spec",
                            Locale.ENGLISH,
                            true,
                            ComponentOrder.NAME,
                            Component.Type.all(),
                            ColumnAttribute.defaultList(),
                            SequenceAttribute.defaultList(),
                            "a4",
                            PageOrientation.PORTRAIT,
                            List.of("10mm", "10mm", "10mm", "10mm")
                        )
                    )
            );

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
            assertThat(config).isEqualTo(DEFAULT_CONFIG);
        }
    }
}
