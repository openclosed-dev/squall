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

import dev.openclosed.squall.api.ServiceException;

import java.util.List;
import java.util.ServiceLoader;

/**
 * Factory of annotations available in doc comments.
 */
public interface DocAnnotationFactory {

    /**
     * Creates an annotation.
     * @param name the name of the annotation.
     * @param content the content given for the annotation.
     * @return created annotation, or {@code null}
     * @throws IllegalArgumentException if the specified content is invalid for the annotation.
     */
    DocAnnotation<?> createAnnotation(String name, String content);

    /**
     * Creates an instance of the factory.
     * @return created factory.
     * @throws ServiceException an error has occurred while loading services.
     */
    static DocAnnotationFactory newInstance() {
        return new CompositeDocAnnotationFactory(loadServices());
    }

    private static List<DocAnnotationFactory> loadServices() {
        try {
            return ServiceLoader.load(DocAnnotationFactory.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        } catch (Exception e) {
            throw new ServiceException(DocAnnotationFactory.class, e);
        }
    }
}
