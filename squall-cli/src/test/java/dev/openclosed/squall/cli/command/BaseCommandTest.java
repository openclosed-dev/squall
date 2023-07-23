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

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.cli.spi.CommandException;

final class BaseCommandTest {

    public record TestCase(String name, String messageKey, String... args) implements BaseTestCase {

        @Override
        public String subcommand() {
            return "init";
        }

        @Override
        public String prefix() {
            return "base";
        }
    }

    private static final Path NONEXISTENT_PATH = BaseTestCase.BASE_WORK_DIR.resolve(
            Path.of("base", "_"));
    private static final Path DUMMY_FILE_PATH = BaseTestCase.BASE_WORK_DIR.resolve(
            Path.of("base", "not-directory", "dummy.txt"));

    public static Stream<TestCase> testCases() {
        return Stream.of(
                new TestCase("nonexistent", "WORKING_DIRECTORY_NOT_EXIST", "-C", NONEXISTENT_PATH.toString()),
                new TestCase("not-directory", "WORKING_DIRECTORY_IS_NOT_DIRECTORY", "-C", DUMMY_FILE_PATH.toString())
                );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    public void testCommand(TestCase testCase) throws IOException {
        var result = testCase.run();
        assertThat(result.exitCode()).isEqualTo(1);

        var thrown = result.thrown().get();
        assertThat(thrown).isInstanceOf(CommandException.class);
        if (thrown instanceof CommandException e) {
            var message = e.getMessageObject();
            assertThat(message.key()).isEqualTo(testCase.messageKey());
        }
    }
}
