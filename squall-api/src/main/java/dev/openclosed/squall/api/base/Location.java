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

/**
 * A location in input source.
 * @param lineNo the line number, starting from 1.
 * @param columnNo the column number, starting from 1.
 * @param offset the offset in characters, starting from 0.
 */
public record Location(int lineNo, int columnNo, long offset) {

    public Location {
        if (lineNo <= 0) {
            throw new IllegalArgumentException("lineNo must be greater than zero");
        }
        if (columnNo <= 0) {
            throw new IllegalArgumentException("columnNo must be greater than zero");
        }
        if (offset < 0L) {
            throw new IllegalArgumentException("offset must not be negative");
        }
    }
}
