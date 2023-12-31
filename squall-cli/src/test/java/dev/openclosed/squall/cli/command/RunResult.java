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

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


public record RunResult(int exitCode, List<String> consoleOutput, Optional<Exception> thrown) {

    private static final Pattern TIME_ELAPSED = Pattern.compile("\\([\\d,]+ ms\\)");
    private static final Pattern FILE_PATH_SEPARATOR = Pattern.compile("\\\\");

    public List<String> getConsoleOutputToVerify() {
        return consoleOutput().stream()
            .map(line -> TIME_ELAPSED.matcher(line).replaceAll("(### ms)"))
            .map(line -> FILE_PATH_SEPARATOR.matcher(line).replaceAll("/"))
            .toList();
    }
}
