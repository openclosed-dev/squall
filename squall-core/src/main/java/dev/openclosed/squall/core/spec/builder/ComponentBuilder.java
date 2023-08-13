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

package dev.openclosed.squall.core.spec.builder;

import dev.openclosed.squall.api.spec.DocAnnotation;

import java.util.List;
import java.util.stream.Stream;

abstract class ComponentBuilder {

    private final String name;
    private final List<String> parents;
    private final List<String> parentsForChild;
    private final List<DocAnnotation> annotations;

    protected ComponentBuilder(String name, List<String> parents, List<DocAnnotation> annotations) {
        this.name = name;
        this.parents = parents;
        this.parentsForChild = concatNameList(parents, name);
        this.annotations = annotations;
    }

    final String name() {
        return name;
    }

    final List<String> parents() {
        return parents;
    }

    final List<String> parentsForChild() {
        return parentsForChild;
    }

    final List<DocAnnotation> annotations() {
        return annotations;
    }

    private static List<String> concatNameList(List<String> parents, String name) {
        if (parents.isEmpty()) {
            return List.of(name);
        } else {
            return Stream.concat(parents.stream(), Stream.of(name)).toList();
        }
    }
}
