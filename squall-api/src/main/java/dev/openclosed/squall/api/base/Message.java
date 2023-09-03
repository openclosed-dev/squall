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

import java.text.MessageFormat;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * A message in resource bundle.
 */
public interface Message extends Supplier<String> {

    static Message of(String key, Object[] args, ResourceBundle bundle) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(args);
        Objects.requireNonNull(bundle);
        record BundledMessage(String key, Object[] args, ResourceBundle bundle) implements Message { }
        return new BundledMessage(key, args, bundle);
    }

    /**
     * Returns the unique identifier of this message.
     * @return the unique identifier of this message.
     */
    String key();

    /**
     * Returns the arguments composing a message.
     * @return the arguments composing a message.
     */
    Object[] args();

    /**
     * Returns the resource bundle for this message.
     * @return the resource bundle found.
     */
    ResourceBundle bundle();

    // Supplier

    /**
     * Returns the formatted message.
     * @return the formatted message.
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
