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

package dev.openclosed.squall.api.renderer;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

public interface RendererFactory {

    String format();

    default Renderer createRenderer(RenderConfig config) {
        return createRenderer(config, config.locale());
    }

    default Renderer createRenderer(RenderConfig config, Locale locale) {
        return createRenderer(config, MessageBundle.forLocale(locale));
    }

    Renderer createRenderer(RenderConfig config, MessageBundle bundle);

    static RendererFactory get(String format) {
        for (var factory : ServiceLoader.load(RendererFactory.class)) {
            if (factory.format().equals(format)) {
                return factory;
            }
        }
        throw new NoSuchElementException("No renderer found for format: " + format);
    }
}
