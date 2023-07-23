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

package dev.openclosed.squall.core.base;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.spi.AbstractResourceBundleProvider;

import dev.openclosed.squall.api.spi.RendererMessagesProvider;

public final class DefaultRendererMessagesProvider extends AbstractResourceBundleProvider
    implements RendererMessagesProvider {

    private static final String BASE_NAME = "dev.openclosed.squall.core.RendererMessages";

    @Override
    public ResourceBundle getBundle(String baseName, Locale locale) {
        if (locale == Locale.ROOT) {
            locale = Locale.ENGLISH;
        }
        return super.getBundle(BASE_NAME, locale);
    }
}
