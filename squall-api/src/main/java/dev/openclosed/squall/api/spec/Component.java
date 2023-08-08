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

package dev.openclosed.squall.api.spec;

import dev.openclosed.squall.api.base.MapSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A component in the database specification.
 */
public interface Component extends MapSource {

    /**
     * Type of the component.
     */
    enum Type {
        DATABASE,
        SCHEMA,
        TABLE,
        COLUMN,
        SEQUENCE
    };

    enum State {
        DEFINED,
        UNDEFINED
    };

    /**
     * Returns the type of this component.
     * @return the type of this component.
     */
    Type type();

    /**
     * Returns the name of this component.
     * @return the name of this component.
     */
    String name();

    /**
     * Returns the qualified name of this component.
     * By default, this method returns the name of the component.
     * @return the qualified name of this component.
     */
    default String qualifiedName() {
        return name();
    }

    /**
     * Returns the list of the annotations attached to this component.
     * @return the list of the annotations.
     */
    default List<DocAnnotation> annotations() {
        return Collections.emptyList();
    }

    /**
     * Returns the state of this component.
     * @return {@code State.DEFINED} if defined explicitly, {@code State.UNDEFINED} otherwise.
     */
    default State state() {
        return State.DEFINED;
    }

    boolean isDeprecated();

    /**
     * Returns the label of this component.
     * @return the label of this component.
     */
    default Optional<String> label() {
        return getFirstAnnotation(DocAnnotationType.LABEL)
                .map(DocAnnotation::value);
    }

    default Optional<String> description() {
        return getFirstAnnotation(DocAnnotationType.DESCRIPTION)
                .map(DocAnnotation::value);
    }

    default  Optional<DocAnnotation> getFirstAnnotation(DocAnnotationType type) {
        return annotations().stream()
                .filter(a -> a.type() == type)
                .findFirst();
    }

    default void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, Component parent) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
