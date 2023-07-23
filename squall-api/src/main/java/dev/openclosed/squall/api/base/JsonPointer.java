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

package dev.openclosed.squall.api.base;

import java.util.List;

/**
 * JSON pointer.
 */
public interface JsonPointer {

    /**
     * Creates an instance of {@link JsonPointer}.
     * @param tokens the reference tokens composing the pointer.
     * @return created instance of JSON pointer.
     */
    static JsonPointer of(List<String> tokens) {
        return JsonPointerImpl.of(tokens);
    }

    /**
     * Returns reference tokens of this pointer.
     * @return reference tokens.
     */
    List<String> tokens();

    /**
     * Return the string representation of this pointer.
     * @return the string representation of this pointer.
     */
    @Override
    String toString();
}
