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

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.base.Message;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Message bundle for SQL parsers.
 */
public interface MessageBundle {

    /**
     * The base name of the resource bundle.
     */
    String BUNDLE_BASE_NAME = "dev.openclosed.squall.api.Messages";

    /**
     * Creates a message bundle for the specified locale.
     * @param locale the locale of the message bundle.
     * @return newly created message bundle.
     */
    static MessageBundle forLocale(Locale locale) {
        Objects.requireNonNull(locale);
        var resourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
        return () -> resourceBundle;
    }

    //CHECKSTYLE:OFF

    /**
     * Message that unexpected end of input was reached.
     * @return the message.
     */
    default Message UNEXPECTED_END_OF_INPUT() {
        return of("UNEXPECTED_END_OF_INPUT");
    }

    /**
     * Message that invalid character was found.
     * @param c the found character.
     * @return the message.
     */
    default Message INVALID_CHARACTER(char c) {
        return of("INVALID_CHARACTER", String.valueOf(c));
    }

    /**
     * Message that syntax error was detected.
     * @param text the text containing the syntax error.
     * @return the message.
     */
    default Message SYNTAX_ERROR(CharSequence text) {
        return of("SYNTAX_ERROR", text);
    }

    //CHECKSTYLE:ON

    /**
     * Returns the resource bundle that provides messages.
     * @return the resource bundle that provides messages.
     */
    ResourceBundle getResourceBundle();

    private Message of(String key, Object... args) {
        return Message.of(key, args, getResourceBundle());
    }
}
