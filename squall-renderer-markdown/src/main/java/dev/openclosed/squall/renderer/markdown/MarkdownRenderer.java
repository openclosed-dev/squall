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

import dev.openclosed.squall.api.renderer.MessageBundle;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.TextRenderer;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

class MarkdownRenderer implements TextRenderer {

    private final RenderConfig config;
    private final MessageBundle bundle;

    MarkdownRenderer(RenderConfig config, MessageBundle bundle) {
        this.config = config;
        this.bundle = bundle;
    }

    @Override
    public void render(DatabaseSpec spec, Path dir) throws IOException {
        Objects.requireNonNull(spec);
        Objects.requireNonNull(dir);
        Files.createDirectories(dir);
        Path path = dir.resolve(this.config.basename() + ".md");
        try (Writer writer = Files.newBufferedWriter(path)) {
            render(spec, writer);
        }
        addAssetFiles(dir);
    }

    @Override
    public String renderToString(DatabaseSpec spec) {
        Objects.requireNonNull(spec);
        try (var writer = new StringWriter()) {
            render(spec, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void render(DatabaseSpec spec, Writer writer) throws IOException {
        new SpecDocumentWriter(this.config, this.bundle, writer).writeSpec(spec);
        writer.flush();
    }

    private void addAssetFiles(Path dir) throws IOException {
        Path imagesDir = dir.resolve("images");
        Files.createDirectories(imagesDir);
        for (Badge badge : Badge.values()) {
            try (var in = badge.getResourceAsStream()) {
                Files.copy(in, imagesDir.resolve(badge.filename()), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
