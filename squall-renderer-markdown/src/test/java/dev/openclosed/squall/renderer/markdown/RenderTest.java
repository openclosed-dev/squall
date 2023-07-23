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

package dev.openclosed.squall.renderer.markdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record RenderTest(
        String title,
        String json,
        List<String> sql,
        String expected) {

    @Override
    public String toString() {
        return title();
    }

    public static List<RenderTest> loadFrom(InputStream in) {
        try (var reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return new TestParser(reader.lines().iterator()).parse();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static class TestParser {

        private final Iterator<String> iterator;

        TestParser(Iterator<String> iterator) {
            this.iterator = iterator;
        }

        List<RenderTest> parse() {
            var tests = new ArrayList<RenderTest>();
            while (iterator.hasNext()) {
                tests.add(parseTest());
            }
            return tests;
        }

        RenderTest parseTest() {
            var title = "";
            var json = "{}";
            var sqlBlocks = new ArrayList<String>();

            while (iterator.hasNext()) {
                var line = iterator.next();
                if (line.startsWith("#")) {
                    title = line.substring(1).trim();
                    break;
                }
            }

            while (iterator.hasNext()) {
                var line = iterator.next();
                if (line.startsWith("```sql")) {
                    sqlBlocks.add(parseCodeBlock());
                } else if (line.startsWith("```json")) {
                    json = parseCodeBlock();
                } else if (line.startsWith("#")) {
                    return new RenderTest(title, json, sqlBlocks, parseExpected(line));
                }
            }

            throw new IllegalStateException();
        }

        private String parseCodeBlock() {
            var builder = new StringBuilder();
            while (iterator.hasNext()) {
                var line = iterator.next();
                if (line.equals("```")) {
                    break;
                }
                if (!builder.isEmpty()) {
                    builder.append('\n');
                }
                builder.append(line);
            }
            return builder.toString();
        }

        private String parseExpected(String first) {
            var builder = new StringBuilder(first);
            while (iterator.hasNext()) {
                var line = iterator.next();
                if (line.trim().equals("---")) {
                    break;
                }
                builder.append('\n').append(line);
            }
            return builder.toString().stripTrailing();
        }
    }
}
