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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.spec.SpecMetadata;

/**
 * The top-level configuration.
 * @param metadata specification metadata.
 * @param sources the SQL source files to be parsed.
 * @param outDir the output directory.
 * @param parser the configuration for the parser
 * @param renderers the configuration for one or more renderers.
 */
public record RootConfig(
    Optional<SpecMetadata> metadata,
    List<String> sources,
    String outDir,
    ParserConfig parser,
    Map<String, RenderConfig> renderers
    ) {

    /**
     * Create a configuration with default settings.
     */
    public RootConfig() {
        this(Optional.empty(),
            Collections.emptyList(),
            "output",
            ParserConfig.getDefault(),
            Collections.emptyMap());
    }
}
