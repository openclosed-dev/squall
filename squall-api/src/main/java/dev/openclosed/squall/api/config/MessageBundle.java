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

package dev.openclosed.squall.api.config;

import dev.openclosed.squall.api.message.Message;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

interface MessageBundle {

    String BUNDLE_BASE_NAME = "dev.openclosed.squall.api.message.Messages";

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

    //CHECKSTYLE:OFF

    default Message UNEXPECTED_END_OF_INPUT() {
        return of("UNEXPECTED_END_OF_INPUT");
    }

    default Message BAD_CONFIGURATION() {
        return of("BAD_CONFIGURATION");
    }

    default Message JSON_ILL_FORMED() {
        return of("JSON_ILL_FORMED");
    }

    default Message TYPE_MISMATCH(Class<?> expectedType, Class<?> actualType) {
        return of("TYPE_MISMATCH", nameOf(expectedType), nameOf(actualType));
    }

    default Message UNSUPPORTED_TARGET_TYPE(Class<?> targetType) {
        return of("UNSUPPORTED_TARGET_TYPE", targetType.getSimpleName());
    }

    default Message ILLEGAL_VALUE(Class<?> enumClass, String actualValue) {
        return of("ILLEGAL_VALUE", enumerators(enumClass), actualValue);
    }

    default Message UNKNOWN_PROPERTY(String name) {
        return of("UNKNOWN_PROPERTY", name);
    }

    //CHECKSTYLE:ON

    ResourceBundle getResourceBundle();

    private Message of(String key, Object... args) {
        return Message.of(key, args, getResourceBundle());
    }

    private static String nameOf(Class<?> cls) {
        if (Map.class.isAssignableFrom(cls)) {
            return "object";
        } else if (Collection.class.isAssignableFrom(cls)) {
            return "array";
        }
        return switch (cls.getSimpleName()) {
            case "String" -> "string";
            case "Integer", "BigInteger" -> "integer";
            case "Float", "Double", "BigDecimal" -> "number";
            default -> cls.getSimpleName().toLowerCase();
        };
    }

    private static String enumerators(Class<?> cls) {
        var sb = new StringBuilder();
        sb.append('[');
        int i = 0;
        for (var value : cls.getEnumConstants()) {
            if (i++ > 0) {
                sb.append(", ");
            }
            sb.append('"')
                .append(value.toString().toLowerCase())
                .append('"');
        }
        return sb.append(']').toString();
    }
}
