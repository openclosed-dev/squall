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

package dev.openclosed.squall.core.config;

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.base.CodeFinder;
import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.config.ConfigurationException;
import dev.openclosed.squall.api.config.MessageBundle;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;
import dev.openclosed.squall.api.spi.JsonReader;
import dev.openclosed.squall.api.spi.JsonReadingException;

/**
 * Default implementation of {@link ConfigLoader}.
 */
public final class DefaultConfigLoader implements ConfigLoader, Consumer<Problem> {

    private final JsonReader reader;
    private final MessageBundle messageBundle;
    private final TypeResolver typeResolver;
    private final ObjectFactory objectFactory;
    private List<Problem> problems;

    public DefaultConfigLoader() {
        this.reader = JsonReader.get();
        this.messageBundle = MessageBundle.forLocale(Locale.getDefault());
        this.typeResolver = TypeResolver.builder().build();
        this.objectFactory = new ObjectFactory(
            this.messageBundle,
            this.typeResolver);
    }

    // ConfigLoader

    @Override
    public RootConfig loadFromJson(String text) {
        return loadFromJson(text, RootConfig.class);
    }

    @Override
    public SpecMetadata loadMetadataFromJson(String text) {
        return loadFromJson(text, SpecMetadata.class);
    }

    @Override
    public RenderConfig loadRenderConfigFromJson(String text) {
        return loadFromJson(text, RenderConfig.class);
    }

    @Override
    public List<Problem> getProblems() {
        return (problems != null) ? problems : Collections.emptyList();
    }

    // Consumer

    @Override
    public void accept(Problem problem) {
        if (problems == null) {
            problems = new ArrayList<>();
        }
        problems.add(problem);
    }

    //

    MessageBundle messages() {
        return this.messageBundle;
    }

    <T extends Record> T loadFromJson(String text, Class<T> targetClass) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(targetClass);
        this.problems = null;
        var map = loadMapFromJson(text);
        return loadFromMap(map, targetClass);
    }

    <T extends Record> T loadFromMap(Map<String, ?> map, Class<T> targetClass) {
        try {
            return objectFactory.createRecord(map, targetClass, this::accept);
        } catch (Exception e) {
            throw new ConfigurationException(messages().BAD_CONFIGURATION(), getProblems());
        }
    }

    private Map<String, ?> loadMapFromJson(String text) {
        if (text.isEmpty()) {
            addProblem(Level.ERROR, messages().UNEXPECTED_END_OF_INPUT());
        } else {
            try {
                return reader.readObject(text);
            } catch (JsonReadingException e) {
                var loc = e.getLocation();
                if (loc.offset() < text.length()) {
                    var source = new CodeFinder(text).findCode(e.getLocation());
                    addProblem(Level.ERROR, messages().JSON_ILL_FORMED(), e.getLocation(), source);
                } else {
                    addProblem(Level.ERROR, messages().UNEXPECTED_END_OF_INPUT());
                }
            }
        }
        throw new ConfigurationException(messages().BAD_CONFIGURATION(), getProblems());
    }

    private void addProblem(Level severity, Message message) {
        accept(new ConfigProblem(severity, message));
    }

    private void addProblem(Level severity, Message message, Location location, Optional<String> source) {
        accept(new ConfigProblem(severity, message, location, source));
    }
}
