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

import dev.openclosed.squall.api.ServiceException;

import java.util.Locale;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * Factory of {@link Renderer}s.
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
     * @return newly created renderer factory.
     * @throws ServiceException if an error has occurred while loading the service.
     */
    static RendererFactory newInstance(String format) {
        Objects.requireNonNull(format);
        try {
            return ServiceLoader.load(RendererFactory.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(factory -> format.equalsIgnoreCase(factory.format()))
                .findFirst()
                .get();
        } catch (Exception e) {
            throw new ServiceException(RendererFactory.class, e);
        }
    }
}
