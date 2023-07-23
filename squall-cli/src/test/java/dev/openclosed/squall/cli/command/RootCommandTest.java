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

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

final class RootCommandTest {

    private static final PrintWriter PRINTER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    @BeforeAll
    public static void setUpOnce() {
        Locale.setDefault(Locale.ENGLISH);
    }

    record SquallTestCase(String name, int exitCode, String... args) {
        @Override
        public String toString() {
            return name();
        }
    }

    public static Stream<SquallTestCase> testCommand() {
        return Stream.of(
                new SquallTestCase("no-args", 0),
                new SquallTestCase("with-help", 0, "--help"),
                new SquallTestCase("with-version", 0, "--version")
                );
    }

    @ParameterizedTest
    @MethodSource
    public void testCommand(SquallTestCase testCase) {
        PRINTER.print("[" + testCase.name() + "] ");
        PRINTER.println("squall " + String.join(" ", testCase.args));
        var command = new RootCommand();
        int exitCode = command.run(PRINTER, PRINTER, testCase.args());
        PRINTER.println();
        assertThat(exitCode).isEqualTo(testCase.exitCode());
    }
}
