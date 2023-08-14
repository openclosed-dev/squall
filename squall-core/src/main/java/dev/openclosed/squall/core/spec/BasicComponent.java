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

package dev.openclosed.squall.core.spec;

import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.ComponentOrder;
import dev.openclosed.squall.api.spec.DocAnnotationType;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.core.base.RecordMapSource;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic implementation of {@link Component}.
 */
interface BasicComponent extends Component, RecordMapSource {

    @Override
    default String fullName() {
        var parents = parents();
        if (parents.isEmpty()) {
            return name();
        } else {
            return Stream.concat(parents.stream(), Stream.of(name()))
                .collect(Collectors.joining("."));
        }
    }

    @Override
    default boolean isDeprecated() {
        return getFirstAnnotation(DocAnnotationType.DEPRECATED).isPresent();
    }

    @Override
    default void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, SpecVisitor.Context context) {
        acceptVisitor(visitor, order, ordinal, (SpecVisitorContext) context);
    }

    void acceptVisitor(SpecVisitor visitor, ComponentOrder order, int ordinal, SpecVisitorContext context);

    default void visitChildren(
        Collection<? extends Component> components,
        SpecVisitor visitor,
        ComponentOrder order,
        SpecVisitorContext context
    ) {
        context.pushComponent(this);
        visitChildComponents(components, visitor, order, context);
        context.popComponent();
    }

    static void visitChildComponents(
        Collection<? extends Component> components,
        SpecVisitor visitor,
        ComponentOrder order,
        SpecVisitorContext context
    ) {

        int ordinal = 1;
        for (var component : order.reorder(components)) {
            component.acceptVisitor(visitor, order, ordinal++, context);
        }
    }
}
