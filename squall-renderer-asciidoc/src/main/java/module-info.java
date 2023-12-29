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

module dev.openclosed.squall.renderer.asciidoc {
    requires dev.openclosed.squall.api;

    requires org.asciidoctor.asciidoctorj.api;
    requires org.commonmark;

    // AsciiDoctor reads special resources in runtime from the packages below.
    opens dev.openclosed.squall.renderer.asciidoc.fonts;
    opens dev.openclosed.squall.renderer.asciidoc.style;
    opens dev.openclosed.squall.renderer.asciidoc.themes;

    provides dev.openclosed.squall.api.renderer.RendererFactory
        with dev.openclosed.squall.renderer.asciidoc.AsciiDocRendererFactory,
            dev.openclosed.squall.renderer.asciidoc.html.HtmlRendererFactory,
            dev.openclosed.squall.renderer.asciidoc.pdf.PdfRendererFactory;
}
