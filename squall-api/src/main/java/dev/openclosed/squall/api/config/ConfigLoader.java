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

package dev.openclosed.squall.api.config;

import java.util.List;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;

/**
 * A loader of the configurations.
 */
public interface ConfigLoader {

    /**
     * Creates an instance of {@link RootConfig} from JSON document.
     * @param text text containing a JSON document.
     * @return created instance of {@link RootConfig}.
     * @throws ConfigurationException if the input text is invalid.
     */
    RootConfig loadFromJson(String text);

    SpecMetadata loadMetadataFromJson(String text);

    /**
     * Creates an instance of {@link RenderConfig} from JSON document.
     * @param text text containing a JSON document.
     * @return created instance of {@link RenderConfig}.
     * @throws ConfigurationException if the input text is invalid.
     */
    RenderConfig loadRenderConfigFromJson(String text);

    /**
     * Returns the problems found while loading the configuration.
     * @return found problems, or empty if no problem was found.
     */
    List<Problem> getProblems();

    /**
     * Creates a new instance of this type of loader.
     * @return newly created instance of loader.
     */
    static ConfigLoader newLoader() {
        return new DefaultConfigLoader();
    }
}
