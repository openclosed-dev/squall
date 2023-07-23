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

package dev.openclosed.squall.core.base;

import java.util.Optional;
import java.util.regex.Pattern;

import dev.openclosed.squall.api.base.Location;

public final class SnippetExtractor {

    private static final Pattern LINE_TERMINATOR = Pattern.compile("\n");

    private final CharSequence text;
    private String[] splitted;

    public SnippetExtractor(CharSequence text) {
        this.text = text;
    }

    public Optional<String> extract(Location location) {
        String[] lines = getLines();
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

    private String[] getLines() {
        if (splitted == null) {
            splitted = LINE_TERMINATOR.split(text, -1);
        }
        return splitted;
    }
}
