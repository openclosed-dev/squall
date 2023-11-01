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

package dev.openclosed.squall.api.sql.annotation;

import java.util.List;
import java.util.Objects;

/**
 * Composite annotation factory.
 */
class CompositeDocAnnotationFactory implements DocAnnotationFactory {

    private final List<DocAnnotationFactory> services;

    CompositeDocAnnotationFactory(List<DocAnnotationFactory> services) {
        this.services = services;
    }

    @Override
    public DocAnnotation<?> createAnnotation(String name, String content) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(content);
        for (var service : this.services) {
            var annotation = service.createAnnotation(name, content);
            if (annotation != null) {
                return annotation;
            }
        }
        return createDefaultAnnotation(name, content);
    }

    private DocAnnotation<?> createDefaultAnnotation(String name, String content) {
        return switch (name) {
            case "description" -> new Description(content);
            case "label" -> new Label(content);
            case "deprecated" -> new Deprecated(content);
            case "see" -> new See(content);
            case "since" -> new Since(content);
            default -> null;
        };
    }
}
