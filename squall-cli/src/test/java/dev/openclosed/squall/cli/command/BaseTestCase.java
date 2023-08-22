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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.file.PathUtils;

/**
 * Test case for a subcommand.
 */
public interface BaseTestCase {

    PrintWriter PRINTER = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

    Path BASE_WORK_DIR = Path.of(".", "target", "test-runs");
    Path BASE_SOURCE_DIR = Path.of("src", "test", "sql");

    Path EXPECTED_OUTPUT_FILE = Path.of("expected-output.txt");

    /**
     * The name of this test case.
     * @return The name of this test case.
     */
    String name();

    /**
     * The name of the command to test.
     * @return the name of the command
     */
    List<String> subcommand();

    default List<String> prefix() {
        return subcommand();
    }

    /**
     * Returns the arguments given to the subcommand.
     * @return the arguments.
     */
    String[] args();

    /**
     * Returns the working directory for this test run.
     * @return the working directory.
     */
    default Path directory() {
        return getTestPath(BASE_WORK_DIR).resolve(name());
    }

    default List<String> getExpectedConsoleOutput() {
        Path path = sourceDirectory().resolve(EXPECTED_OUTPUT_FILE);
        if (!Files.exists(path)) {
            return Collections.emptyList();
        }
        try {
            return Files.readAllLines(path).stream().toList();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Runs the subcommand.
     * @return the result of the command execution.
     */
    default RunResult run() {
        var workDir = prepareWorkDirectory();
        var args = createArgs();
        printCommandLine(args);
        var stringWriter = new StringWriter();
        var printWriter = new PrintWriter(stringWriter, true);
        var command = new RootCommand(workDir);
        int exitCode = command.run(printWriter, printWriter, args);
        List<String> consoleOutput = List.of(stringWriter.toString().split("\r?\n"));
        printConsoleOutput(consoleOutput);
        return new RunResult(exitCode, consoleOutput, command.getExecutionException());
    }

    private void printCommandLine(String[] args) {
        PRINTER.print("[" + name() + "] ");
        PRINTER.println("squall " + String.join(" ", args));
    }

    private void printConsoleOutput(List<String> lines) {
        for (String line : lines) {
            PRINTER.println(line);
        }
        PRINTER.flush();
    }

    /**
     * Returns the path to the directory where this test case runs.
     * @return the path to the directory
     */
    private Path prepareWorkDirectory() {
        Path workPath = directory();
        try {
            Files.createDirectories(workPath);
            PathUtils.cleanDirectory(workPath);
            copySources(workPath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return workPath;
    }

    private String[] createArgs() {
        return Stream.concat(subcommand().stream(), Arrays.stream(args()))
            .toArray(String[]::new);
    }

    private Path sourceDirectory() {
        return getTestPath(BASE_SOURCE_DIR).resolve(name());
    }

    private void copySources(Path targetPath) throws IOException {
        Path sourceDir = sourceDirectory();
        if (!Files.exists(sourceDir)) {
            return;
        }
        try (var stream = Files.newDirectoryStream(sourceDir, BaseTestCase::filterSource)) {
            stream.forEach(source -> {
                try {
                    Files.copy(source, targetPath.resolve(source.getFileName()));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    private Path getTestPath(Path basePath) {
        Path path = basePath;
        for (var prefix : prefix()) {
            path = path.resolve(prefix);
        }
        return path;
    }

    private static boolean filterSource(Path path) {
        return Files.isRegularFile(path) && !path.endsWith(EXPECTED_OUTPUT_FILE);
    }
}
