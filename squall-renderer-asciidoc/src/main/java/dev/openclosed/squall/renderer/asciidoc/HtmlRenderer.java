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

package dev.openclosed.squall.renderer.asciidoc;

import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.TextRenderer;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.Options;
import org.asciidoctor.Placement;
import org.asciidoctor.SafeMode;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ResourceBundle;

final class HtmlRenderer implements TextRenderer {

    private final AsciiDocRenderer asciiDocRenderer;
    private static final String BACKEND_NAME = "html5";

    HtmlRenderer(RenderConfig config, ResourceBundle bundle) {
        this.asciiDocRenderer = new AsciiDocRenderer(config, bundle);
    }

    @Override
    public void render(DatabaseSpec spec, Path dir) throws IOException {
        Path path = this.asciiDocRenderer.renderToFile(spec, dir);
        try (var asciidoctor = Asciidoctor.Factory.create()) {
            asciidoctor.convertFile(
                path.toFile(),
                Options.builder()
                    .backend(BACKEND_NAME)
                    .toFile(true)
                    .safe(SafeMode.UNSAFE)
                    .attributes(getAttributes())
                    .build()
            );
        }
    }

    @Override
    public String renderToString(DatabaseSpec spec) {
        String asciiDocText = this.asciiDocRenderer.renderToString(spec);
        return asciiDocText;
    }

    private static Attributes getAttributes() {
        return Attributes.builder()
            .sectionNumbers(true)
            .tableOfContents(true)
            .tableOfContents(Placement.LEFT)
            .build();
    }
}
