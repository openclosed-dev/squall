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

import dev.openclosed.squall.api.message.BaseMessageBundle;

import java.util.Locale;

/**
 * Message bundle for renderers.
 */
public interface MessageBundle extends BaseMessageBundle {

    /**
     * Creates a message bundle for the specified locale.
     * @param locale the locale of the message bundle.
     * @return newly created message bundle.
     */
    static MessageBundle forLocale(Locale locale) {
        var resourceBundle = BaseMessageBundle.getResourceBundleForLocale(locale);
        return () -> resourceBundle;
    }

    /**
     * Returns the header title for a column.
     * @param name the name of the column.
     * @return the header title for a column.
     */
    default String columnHeader(String name) {
        return getString("column.header." + name);
    }

    /**
     * Returns thw word "deprecated" for the current locale.
     * @return thw word "deprecated" for the current locale.
     */
    default String deprecated() {
        return getString("deprecated");
    }
}
