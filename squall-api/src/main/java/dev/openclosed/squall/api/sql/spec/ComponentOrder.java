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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Order of {@link Component}s.
 */
public enum ComponentOrder {
    /** Alphabetical order. */
    NAME {
        @Override
        protected Comparator<Component> comparator() {
            return Comparator.comparing(Component::name);
        }
    },
    /** Definition order. */
    DEFINITION {
        @Override
        public <T extends Component> Collection<T> reorder(Collection<T> collection) {
            return collection;
        }

        @Override
        public <T extends Component> Stream<T> reorder(Stream<T> stream) {
            return stream;
        }
    };

    /**
     * Reorders the components.
     * @param components the list of components.
     * @return reordered components.
     * @param <T> the type of the component.
     */
    public <T extends Component> Collection<T> reorder(Collection<T> components) {
        var list = new ArrayList<T>(components);
        Collections.sort(list, comparator());
        return list;
    }

    /**
     * Returns the components sorted in this order.
     * @param stream the original stream of components.
     * @return the sorted components.
     * @param <T> the type of the components.
     */
    public <T extends Component> Stream<T> reorder(Stream<T> stream) {
        return stream.sorted(comparator());
    }

    /**
     * Returns the comparator of components.
     * @return the comparator of components.
     */
    protected Comparator<Component> comparator() {
        throw new UnsupportedOperationException();
    }
}
