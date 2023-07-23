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

package dev.openclosed.squall.cli.base;

import java.util.Locale;
import java.util.ResourceBundle;

import dev.openclosed.squall.api.base.Message;

/**
 * Bundled message for this module.
 * @param key the key of the message.
 * @param args the arguments which will be passed to the formatter.
 */
public record BundledMessage(String key, Object[] args) implements Message {

    private static final String BUNDLE_BASE_NAME = "dev.openclosed.squall.cli.Messages";

    static Message of(String key, Object... args) {
        return new BundledMessage(key, args);
    }

    @Override
    public ResourceBundle getBundle(Locale locale) {
        return ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }
}
