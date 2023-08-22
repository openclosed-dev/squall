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

package dev.openclosed.squall.cli.command.doc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import dev.openclosed.squall.cli.command.RunResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.cli.command.BaseTestCase;

public final class BuildTest {

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
    }

    record TestCase(String name, int exitCode, String... args) implements BaseTestCase {

        private static final List<String> SUBCOMMAND = List.of("doc", "build");

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
            new TestCase("bad-source", 1),
            new TestCase("bad-source-multiple", 1),
            new TestCase("default", 0),
            new TestCase("help", 0, "--help"),
            new TestCase("json", 0),
            new TestCase("markdown", 0),
            new TestCase("multiple", 0, "json", "markdown"),
            new TestCase("nonexistent-renderer", 1, "markdown"),
            new TestCase("nonexistent-sql", 1),
            new TestCase("no-renderers", 1),
            new TestCase("no-sources", 0),
            new TestCase("redmine", 0),
            new TestCase("unknown-format", 1)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testCommand(TestCase testCase) {
        RunResult result = testCase.run();
        assertThat(result.exitCode()).isEqualTo(testCase.exitCode());
        assertThat(result.getConsoleOutputToVerify()).isEqualTo(testCase.getExpectedConsoleOutput());
    }
}
