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

package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.spec.Component;

/**
 * Writer of component attributes.
 * @param <T> type of spec component.
 */
interface AttributeWriter<T extends Component> {

    String ALIGN_LEFT = ":--";
    String ALIGN_RIGHT = "--:";
    String ALIGN_CENTER = ":-:";

    String name();

    default String getSeparator() {
        return ALIGN_LEFT;
    }

    void writeValue(T component, int rowNo, DocBuilder builder, WriterContext context);
}
