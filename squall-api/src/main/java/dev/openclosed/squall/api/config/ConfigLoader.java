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

import dev.openclosed.squall.api.text.Problem;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;

/**
 * Loader of configurations.
 */
public interface ConfigLoader {

    /**
     * Loads a {@link RootConfig} from the input JSON text.
     * @param text the input text in JSON object format.
     * @return loaded {@link RootConfig}.
     * @throws ConfigurationException if an error has occurred while loading The configuration.
     */
    RootConfig loadFromJson(String text);

    /**
     * Loads a {@link SpecMetadata} from the input JSON text.
     * @param text the input text in JSON object format.
     * @return loaded {@link SpecMetadata}.
     * @throws ConfigurationException if an error has occurred while loading The configuration.
     */
    SpecMetadata loadMetadataFromJson(String text);

    /**
     * Loads a {@link RenderConfig} from the input JSON text.
     * @param text the input text in JSON object format.
     * @return loaded {@link RenderConfig}.
     * @throws ConfigurationException if an error has occurred while loading The configuration.
     */
    RenderConfig loadRenderConfigFromJson(String text);

    /**
     * Returns the problems found while loading the configuration.
     * @return found problems, or empty if there are no problems.
     */
    List<Problem> getProblems();

    /**
     * Creates a new instance of configuration loader.
     * @return newly created instance of configuration loader.
     */
    static ConfigLoader newLoader() {
        return new DefaultConfigLoader();
    }
}
