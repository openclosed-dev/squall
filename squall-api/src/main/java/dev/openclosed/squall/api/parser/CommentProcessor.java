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

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.text.Location;

/**
 * processor of comments in SQL language.
 */
public interface CommentProcessor {

    /**
     * Checks whether this processor can process the given comment text or not.
     * @param text the comment text to process.
     * @return {@code true} if this processor can process the text, {@code false} otherwise.
     */
    boolean canProcess(CharSequence text);

    /**
     * Processes the comment text.
     * This method is called if and only if {@link #canProcess(CharSequence)} returns {@code true}.
     * @param text the comment text to process.
     * @param location the location of the text in the whole text.
     * @param context the context of the parser.
     */
    void processComment(CharSequence text, Location location, ParserContext context);

    /**
     * A comment processor that ignores all incoming comments.
     */
    CommentProcessor NOOP = new CommentProcessor() {

        @Override
        public boolean canProcess(CharSequence text) {
            return false;
        }

        @Override
        public void processComment(CharSequence text, Location location, ParserContext context) {
            // Does nothing
        }
    };

    /**
     * Returns a comment processor that ignores all incoming comments.
     * @return a comment processor that ignores all incoming comments.
     */
    static CommentProcessor noop() {
        return NOOP;
    }
}
