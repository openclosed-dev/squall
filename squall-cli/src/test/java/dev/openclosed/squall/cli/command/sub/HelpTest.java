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

package dev.openclosed.squall.cli.command.sub;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.cli.command.BaseTestCase;

final class HelpTest {

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
    }

    record TestCase(String name, int exitCode, String... args) implements BaseTestCase {

        @Override
        public String subcommand() {
            return "help";
        }

        @Override
        public String toString() {
            return name();
        }
    }

    static Stream<TestCase> testCases() {
        return Stream.of(
                new TestCase("helo", 0),
                new TestCase("init", 0, "init"),
                new TestCase("render", 0, "render")
                );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testCommand(TestCase testCase) {
        int exitCode = testCase.run().exitCode();
        assertThat(exitCode).isEqualTo(testCase.exitCode());
    }
}
