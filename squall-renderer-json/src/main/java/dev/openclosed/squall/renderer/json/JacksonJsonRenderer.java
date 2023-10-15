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

package dev.openclosed.squall.renderer.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.openclosed.squall.api.util.Records;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.TextRenderer;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;

final class JacksonJsonRenderer implements TextRenderer {

    private final RenderConfig config;
    private final ObjectMapper objectMapper;

    JacksonJsonRenderer(RenderConfig config) {
        this.config = config;
        this.objectMapper = buildObjectMapper(config);
    }

    @Override
    public void render(DatabaseSpec spec, Path dir) throws IOException {
        Files.createDirectories(dir);
        Path path = dir.resolve(config.basename() + ".json");
        try (Writer writer = Files.newBufferedWriter(path)) {
            render(spec, writer);
        }
    }

    @Override
    public String renderToString(DatabaseSpec spec) {
        try {
            var writer = new StringWriter();
            render(spec, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void render(DatabaseSpec spec, Writer writer) throws IOException {
        this.objectMapper.writeValue(writer, Records.toMap(spec));
    }

    private ObjectMapper buildObjectMapper(RenderConfig config) {
        return JsonMapper.builder()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .build();
    }
}
