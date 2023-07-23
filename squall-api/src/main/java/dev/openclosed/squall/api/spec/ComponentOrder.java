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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public enum ComponentOrder {
    NAME {
        @Override
        protected Comparator<Component> comparator() {
            return Comparator.comparing(Component::name);
        }
    },
    DEFINITION {
        @Override
        public <T extends Component> Collection<T> reorder(Collection<T> collection) {
            return collection;
        }
    };

    protected Comparator<Component> comparator() {
        throw new UnsupportedOperationException();
    }

    public <T extends Component> Collection<T> reorder(Collection<T> collection) {
        var list = new ArrayList<T>(collection);
        Collections.sort(list, comparator());
        return list;
    }
}
