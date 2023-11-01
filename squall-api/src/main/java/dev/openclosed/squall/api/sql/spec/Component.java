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

package dev.openclosed.squall.api.sql.spec;

import dev.openclosed.squall.api.sql.annotation.Deprecated;
import dev.openclosed.squall.api.sql.annotation.Description;
import dev.openclosed.squall.api.sql.annotation.DocAnnotation;
import dev.openclosed.squall.api.sql.annotation.Label;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Component in a database design specification.
 */
public interface Component {

    /**
     * Type of the component.
     */
    enum Type {
        /** The type is database. */
        DATABASE,
        /** The type is schema. */
        SCHEMA,
        /** The type is table. */
        TABLE,
        /** The type is table column. */
        COLUMN,
        /** The type is sequence. */
        SEQUENCE;

        private static final Set<Type> ALL = Set.of(Type.values());

        /**
         * Returns the set of values defined in this enum.
         * @return the set of values defined in this enum.
         */
        public static Set<Type> all() {
            return ALL;
        }
    }

    /**
     * The states of the component.
     */
    enum State {
        /** The component is defined explicitly. */
        DEFINED,
        /** The component is not defined. */
        UNDEFINED
    }

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
     * Returns the parents of this component.
     * @return the parents of this component.
     * The first element is a database, and the last element is the direct parent
     * of the component.
     */
    default List<String> parents() {
        return Collections.emptyList();
    }

    /**
     * Returns the schema-qualified name of this component.
     * @return the schema-qualified name of this component
     * if this component is not a schema object, the method returns
     * the same value as {@link #name()}.
     */
    default String qualifiedName() {
        return name();
    }

    /**
     * Returns the fully qualified name of this component, including the parents.
     * @return the fully qualified name.
     */
    default String fullName() {
        var parents = parents();
        if (parents.isEmpty()) {
            return name();
        } else {
            return Stream.concat(parents.stream(), Stream.of(name()))
                .collect(Collectors.joining("."));
        }
    }

    /**
     * Returns the list of the annotations attached to this component.
     * @return the list of the annotations.
     */
    default List<DocAnnotation<?>> annotations() {
        return Collections.emptyList();
    }

    /**
     * Returns the state of this component.
     * @return {@code State.DEFINED} if defined explicitly, {@code State.UNDEFINED} otherwise.
     */
    default State state() {
        return State.DEFINED;
    }

    /**
     * Returns whether this component is deprecated or not.
     * @return {@code true} if this component is deprecated, {@code false} otherwise.
     */
    default boolean isDeprecated() {
        return getFirstAnnotationOf(Deprecated.class).isPresent();
    }

    /**
     * Returns the label of this component.
     * @return the label of this component, or empty.
     */
    default Optional<String> label() {
        return getFirstAnnotationOf(Label.class)
                .map(DocAnnotation::value);
    }

    /**
     * Returns the description of this component.
     * @return the description of this component, or empty.
     */
    default Optional<String> description() {
        return getFirstAnnotationOf(Description.class)
                .map(DocAnnotation::value);
    }

    /**
     * Returns the first annotation of the specified class.
     * @param clazz the class of the annotation, must not be {@code null}.
     * @return the first annotation of the specified class, or empty if there is no such annotation.
     * @param <T> the type of the annotation.
     */
    default <T extends DocAnnotation<?>> Optional<T> getFirstAnnotationOf(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return annotations().stream()
                .filter(clazz::isInstance)
                .findFirst()
                .map(clazz::cast);
    }

    /**
     * Accepts a visitor of components.
     * @param visitor the spec component visitor.
     */
    void accept(SpecVisitor visitor);

    /**
     * Returns the children of this component.
     * @param order the order of the children.
     * @return sorted children.
     */
    default Stream<? extends Component> children(ComponentOrder order) {
        return Stream.empty();
    }
}
