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

package dev.openclosed.squall.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class TestCaseParser<T> {

    private final TestCaseBuilder<T> builder;
    private final List<T> testCases = new ArrayList<>();

    public static <T> TestCaseParser<T> withBuilder(TestCaseBuilder<T> builder) {
        return new TestCaseParser<T>(builder);
    }

    private TestCaseParser(TestCaseBuilder<T> builder) {
        this.builder = builder;
    }

    public List<T> parse(InputStream in) {
        try (var reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8))) {
            parseTestCases(reader.lines().iterator());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return testCases;
    }

    private void parseTestCases(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("#")) {
                parseTestCases(iterator, line);
            }
        }
    }

    private void parseTestCases(Iterator<String> iterator, String firstLine) {
        var lines = new ArrayList<String>();
        lines.add(firstLine);
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("#")) {
                parseTestCase(lines);
                lines.clear();
            }
            lines.add(line);
        }

        if (!lines.isEmpty()) {
            parseTestCase(lines);
        }
    }

    private void parseTestCase(List<String> lines) {
        var iterator = lines.iterator();
        String line = iterator.next();
        String title = line.substring(1).strip();

        builder.setTitle(title);

        while (iterator.hasNext()) {
            line = iterator.next();
            if (line.startsWith("```")) {
                String language = line.substring(3).strip();
                parseCodeBlock(language, iterator);
            }
        }

        this.testCases.add(builder.build());
    }

    private void parseCodeBlock(String language, Iterator<String> iterator) {
        var sb = new StringBuilder();
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.strip().equals("```")) {
                this.builder.addCode(sb.toString(), language);
                break;
            }
            if (!sb.isEmpty()) {
                sb.append('\n');
            }
            sb.append(line);
        }
    }
}
