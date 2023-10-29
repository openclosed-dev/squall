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

package dev.openclosed.squall.parser.basic;

import dev.openclosed.squall.api.text.Location;

/**
 * A tokenizer for SQL sources.
 */
public interface SqlTokenizer {

    /**
     * Returns the input text.
     * @return the input text.
     */
    CharSequence text();

    /**
     * Returns the next token.
     * @return the next token.
     */
    Token next();

    /**
     * Consumes the current token.
     */
    void consume();

    /**
     * Returns the current offset.
     * @return the current offset.
     */
    int getOffset();

    /**
     * Returns the offset of the current token.
     * @return the offset of the current token.
     */
    int getTokenOffset();

    /**
     * Returns the current location.
     * @return the current location.
     */
    Location getLocation();

    /**
     * Returns the location of the current token.
     * @return the location of the current token.
     */
    Location getTokenLocation();

    /**
     * Returns the text of the current token.
     * @return the text of the current token.
     */
    default CharSequence getTokenText() {
        return text().subSequence(getTokenOffset(), getOffset());
    }
}
