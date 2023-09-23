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

package dev.openclosed.squall.renderer.asciidoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MarkdownConverterTest {

    private StringWriter stringWriter;
    private MarkdownConverter converter;

    @BeforeEach
    public void setUp() {
        this.stringWriter = new StringWriter();
        this.converter = new MarkdownConverter(new DocBuilder(this.stringWriter));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "basic"
    })
    public void test(String name) throws IOException {
        String markdown = readText(name + ".md");
        String expected = readText(name + ".adoc");
        this.converter.writeText(markdown);
        String actual = this.stringWriter.toString();
        assertThat(actual).isEqualTo(expected);
    }

    private String readText(String resourceName) throws IOException {
        var in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            return null;
        }
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n", "", "\n"));
        }
    }
}
