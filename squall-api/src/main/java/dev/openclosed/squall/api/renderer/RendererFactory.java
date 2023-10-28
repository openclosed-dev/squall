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
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Factory of renderers.
 */
public interface RendererFactory {

    /**
     * Returns the output format that this factory supports.
     * @return the output format that this factory supports, such as "html".
     */
    String format();

    /**
     * Creates a renderer.
     * @param config the configuration for the renderer.
     * @return the created renderer.
     */
    default Renderer createRenderer(RenderConfig config) {
        return createRenderer(config, config.locale());
    }

    /**
     * Creates a renderer for the specified locale.
     * @param config the configuration for the renderer.
     * @param locale the locale by the renderer.
     * @return the created renderer.
     */
    default Renderer createRenderer(RenderConfig config, Locale locale) {
        return createRenderer(config, MessageBundle.forLocale(locale));
    }

    /**
     * Creates a renderer with the specified message bundle.
     * @param config the configuration for the renderer.
     * @param bundle the message bundle used by the renderer.
     * @return the created renderer.
     */
    Renderer createRenderer(RenderConfig config, MessageBundle bundle);

    /**
     * Creates a renderer factory for the specified format.
     * @param format the output format.
     * @return newly created factory.
     * @throws IllegalArgumentException if appropriate factory was not found for the format.
     */
    static RendererFactory newFactory(String format) {
        Objects.requireNonNull(format);
        for (var factory : ServiceLoader.load(RendererFactory.class)) {
            if (factory.format().equals(format)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("RendererFactory was not found for the format: " + format);
    }
}
