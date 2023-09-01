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
import dev.openclosed.squall.api.renderer.Renderer;
import dev.openclosed.squall.api.renderer.RendererFactory;

import java.util.ResourceBundle;

public final class AsciiDocRendererFactory implements RendererFactory {

    @Override
    public String format() {
        return "asciidoc";
    }

    @Override
    public Renderer createRenderer(RenderConfig config, ResourceBundle bundle) {
        return new AsciiDocRenderer(config, bundle);
    }
}
