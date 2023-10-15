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

package dev.openclosed.squall.core.sql.spec;

import dev.openclosed.squall.api.sql.spec.Component;
import dev.openclosed.squall.api.sql.spec.DocAnnotationType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Basic implementation of {@link Component}.
 */
interface BasicComponent extends Component {

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
}
