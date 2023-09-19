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

import dev.openclosed.squall.api.renderer.MessageBundle;
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

final class HtmlRenderer implements TextRenderer {

    private static final String STYLE_DIR = AsciiDocRenderer.RESOURCE_DIR + "/style";

    private final AsciiDocRenderer asciiDocRenderer;
    private static final String BACKEND_NAME = "html5";
    private final Attributes attributes;

    HtmlRenderer(RenderConfig config, MessageBundle bundle) {
        this.asciiDocRenderer = new AsciiDocRenderer(config, bundle);
        this.attributes = buildAttributes(config);
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
                    .attributes(this.attributes)
                    .build()
            );
        }
    }

    @Override
    public String renderToString(DatabaseSpec spec) {
        return this.asciiDocRenderer.renderToString(spec);
    }

    private static Attributes buildAttributes(RenderConfig config) {
        var builder = Attributes.builder()
            .attribute("lang", config.locale().getLanguage())
            .sectionNumbers(config.numbering())
            .tableOfContents(true)
            .tableOfContents(Placement.LEFT)
            .stylesDir(STYLE_DIR);

        switch (config.locale().getLanguage()) {
            case "ja" -> {
                builder.styleSheetName("style-ja.css");
            }
            default -> {
                builder.styleSheetName("style.css");
            }
        }

        return builder.build();
    }
}
