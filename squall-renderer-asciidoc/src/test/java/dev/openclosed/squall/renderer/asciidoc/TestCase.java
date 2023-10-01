/*
 * Copyright 2023 The Squall Authors
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

import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.Dialect;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public record TestCase(
    String title, String sqlName, String configName, String metadata, Dialect dialect) {

    private static ConfigLoader configLoader;

    String sqlFileName() {
        return dialect().dialectName() + "/" + sqlName() + ".sql";
    }

    String configFileName() {
        return configName() + ".json";
    }

    String metadataFileName() {
        return metadata() + ".json";
    }

    String expectedTextFileName() {
        return dialect().dialectName() + "/" + title() + ".expected.adoc";
    }

    String getSql() throws IOException {
        return readText(sqlFileName());
    }

    RenderConfig getConfig() throws IOException {
        return getConfigLoader().loadRenderConfigFromJson(readText(configFileName()));
    }

    SpecMetadata getMetadata() throws IOException {
        return getConfigLoader().loadMetadataFromJson(readText(metadataFileName()));
    }

    Optional<String> getExpectedText() throws IOException {
        return Optional.ofNullable(readText(expectedTextFileName()));
    }

    @Override
    public String toString() {
        return title();
    }

    private ConfigLoader getConfigLoader() {
        if (configLoader == null) {
            configLoader = ConfigLoader.get();
        }
        return configLoader;
    }

    private String readText(String resourceName) throws IOException {
        var in = getClass().getResourceAsStream(resourceName);
        if (in == null) {
            return null;
        }
        try (var reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
