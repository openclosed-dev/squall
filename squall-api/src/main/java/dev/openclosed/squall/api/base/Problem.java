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

import java.lang.System.Logger.Level;
import java.util.Optional;

/**
 * Problem found in the input text.
 */
public interface Problem {

    /**
     * Returns the severity of this problem.
     * @return the severity of this problem.
     */
    Level severity();

    /**
     * Returns the description of this problem.
     * @return the description of this problem as a {@link Message}.
     */
    Message message();

    /**
     * Returns the location where this problem has occurred.
     * @return the location including line and column numbers, may be empty.
     */
    default Optional<Location> location() {
        return Optional.empty();
    }

    /**
     * Returns the location where this problem has occurred.
     * @return the location as a JSON pointer, may be empty.
     */
    default Optional<JsonPointer> pointer() {
        return Optional.empty();
    }

    /**
     * Returns the text fragment where this problem has occurred.
     * @return the text fragment as a string, may be empty.
     */
    default Optional<String> source() {
        return Optional.empty();
    }

    /**
     * Returns the string representation of this problem.
     * @return the string representation of this problem.
     */
    @Override
    String toString();
}
