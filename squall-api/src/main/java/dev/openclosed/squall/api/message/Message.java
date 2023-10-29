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

package dev.openclosed.squall.api.message;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Message in a resource bundle.
 */
public interface Message extends Supplier<String> {

    /**
     * Creates a message.
     * @param key the unique identifier in a resource bundle.
     * @param args the arguments used for formatting the message.
     * @param bundle the resource bundle providing the message.
     * @return created message.
     */
    static Message of(String key, Object[] args, ResourceBundle bundle) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(args);
        Objects.requireNonNull(bundle);
        record BundledMessage(String key, Object[] args, ResourceBundle bundle) implements Message { }
        return new BundledMessage(key, args, bundle);
    }

    /**
     * Returns the identifier of this message, that is unique in the resource bundle.
     * @return the identifier of this message, that is unique in the resource bundle.
     */
    String key();

    /**
     * Returns the arguments given to this message.
     * @return the arguments given to this message.
     */
    Object[] args();

    /**
     * Returns the resource bundle that provides this message.
     * @return the resource bundle that provides this message.
     */
    ResourceBundle bundle();

    // Supplier

    /**
     * Returns the string formatted with arguments.
     * @return the string formatted with arguments.
     */
    @Override
    default String get() {
        var pattern = bundle().getString(key());
        if (args().length > 0) {
            return MessageFormat.format(pattern, args());
        } else {
            return pattern;
        }
    }
}
