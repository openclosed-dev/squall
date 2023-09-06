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

package dev.openclosed.squall.api.renderer;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public interface MessageBundle {

    String BUNDLE_BASE_NAME = "dev.openclosed.squall.api.Messages";

    /**
     * Creates a message bundle.
     * @param locale the locale of the message bundle.
     * @return newly created message bundle.
     */
    static MessageBundle forLocale(Locale locale) {
        Objects.requireNonNull(locale);
        var resourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
        return () -> resourceBundle;
    }

    default String columnHeader(String name) {
        return get("column.header." + name);
    }

    default String deprecated() {
        return get("deprecated");
    }

    ResourceBundle getResourceBundle();

    private String get(String key) {
        return getResourceBundle().getString(key);
    }
}
