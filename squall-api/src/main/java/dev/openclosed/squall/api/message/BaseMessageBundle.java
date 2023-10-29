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

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Base type for message bundles.
 */
public interface BaseMessageBundle {

    /**
     * The base name of the resource bundle.
     */
    String BUNDLE_BASE_NAME = BaseMessageBundle.class.getPackageName() + ".Messages";

    /**
     * Returns the resource bundle for the specified locale.
     * @param locale the locale of the resource bundle.
     * @return found resource bundle.
     */
    static ResourceBundle getResourceBundleForLocale(Locale locale) {
        Objects.requireNonNull(locale);
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }

    /**
     * Returns the resource bundle that provides messages.
     * @return the resource bundle that provides messages.
     */
    ResourceBundle getResourceBundle();

    /**
     * Returns the string identified by key.
     * @param key the key of the message.
     * @return the string found in the resource bundle.
     */
    default String getString(String key) {
        return getResourceBundle().getString(key);
    }

    /**
     * Returns the message composed with key and arguments.
     * @param key the key of the message.
     * @param args the arguments passed to the message.
     * @return found message.
     */
    default Message of(String key, Object... args) {
        return Message.of(key, args, getResourceBundle());
    }
}
