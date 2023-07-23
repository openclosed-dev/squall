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

package dev.openclosed.squall.cli.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.cli.spi.CommandException;

final class ConfigurableCommandTest {

    public record TestCase(String name, String messageKey, String... args) implements BaseTestCase {

        @Override
        public String subcommand() {
            return "render";
        }

        @Override
        public String prefix() {
            return "configurable";
        }

        @Override
        public String toString() {
            return name();
        }
    }

    public static Stream<TestCase> testCases() {
        return Stream.of(
                new TestCase("nonexistent", "CONFIGURATION_NOT_EXIST"),
                new TestCase("empty", "CONFIGURATION_INVALID"),
                new TestCase("ill-formed", "CONFIGURATION_INVALID", "-f", "squall.txt")
                );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testCommand(TestCase testCase) throws IOException {
        var result = testCase.run();
        assertThat(result.exitCode()).isEqualTo(1);

        assertThat(result.consoleOutput()).isEqualTo(testCase.getExpectedConsoleOutput());

        var thrown = result.thrown().get();
        assertThat(thrown).isInstanceOf(CommandException.class);
        if (thrown instanceof CommandException e) {
            var message = e.getMessageObject();
            assertThat(message.key()).isEqualTo(testCase.messageKey());
        }
    }
}
