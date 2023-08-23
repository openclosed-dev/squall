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

package dev.openclosed.squall.parser;

import dev.openclosed.squall.api.base.Message;

import static dev.openclosed.squall.parser.BundledMessage.of;

/**
 * Messages for internal use.
 */
public final class Messages {

    //CHECKSTYLE:OFF

    // Common

    public static Message UNEXPECTED_END_OF_INPUT() {
        return of("UNEXPECTED_END_OF_INPUT");
    }

    // SQL sources

    public static Message INVALID_CHARACTER(char c) {
        return of("INVALID_CHARACTER", String.valueOf(c));
    }

    public static Message SYNTAX_ERROR(CharSequence text) {
        return of("SYNTAX_ERROR", text);
    }

    // Doc comment

    public static Message UNKNOWN_ANNOTATION(String name) {
        return of("UNKNOWN_ANNOTATION", name);
    }

    //CHECKSTYLE:ON

    private Messages() {
    }
}
