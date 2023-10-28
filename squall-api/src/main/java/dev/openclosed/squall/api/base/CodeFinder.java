/*
 * Copyright 2023 The Squall Authors
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

package dev.openclosed.squall.api.base;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Finder of code fragments.
 */
public class CodeFinder {

    private static final Pattern LINE_TERMINATOR = Pattern.compile("\n");

    private final CharSequence text;
    private final String[] split;

    /**
     * Constructs a finder.
     * @param text the whole text of source code.
     */
    public CodeFinder(CharSequence text) {
        Objects.requireNonNull(text);
        this.text = text;
        this.split = LINE_TERMINATOR.split(text, -1);
    }

    /**
     * Finds a code fragment at the specified location.
     * @param location the location to find code fragment.
     * @return the code found fragment, or empty if not exists.
     */
    public Optional<String> findCode(Location location) {
        String[] lines = this.split;
        if (location.offset() < text.length()) {
            String line = lines[location.lineNo() - 1];
            String spaces = line.substring(0, location.columnNo() - 1).replaceAll("\\S", " ");
            return Optional.of(
                new StringBuilder()
                    .append(line).append('\n')
                    .append(spaces).append('^')
                    .toString());
        } else {
            return Optional.empty();
        }
    }
}
