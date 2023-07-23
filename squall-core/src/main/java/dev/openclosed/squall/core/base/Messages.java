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

import static dev.openclosed.squall.core.base.BundledMessage.of;

import java.util.Collection;
import java.util.Map;

import dev.openclosed.squall.api.base.Message;

/**
 * Messages for internal use.
 */
public final class Messages {

    //CHECKSTYLE:OFF

    // Common

    public static Message UNEXPECTED_END_OF_INPUT() {
        return of("UNEXPECTED_END_OF_INPUT");
    }

    // Configuration

    public static Message BAD_CONFIGURATION() {
        return of("BAD_CONFIGURATION");
    }

    public static Message JSON_BLANK() {
        return of("JSON_BLANK");
    }

    public static Message JSON_NOT_OBJECT() {
        return of("JSON_NOT_OBJECT");
    }

    public static Message JSON_ILL_FORMED() {
        return of("JSON_ILL_FORMED");
    }

    public static Message TYPE_MISMATCH(Class<?> expectedType, Class<?> actualType) {
        return of("TYPE_MISMATCH", nameOf(expectedType), nameOf(actualType));
    }

    public static Message UNSUPPORTED_TARGET_TYPE(Class<?> targetType) {
        return of("UNSUPPORTED_TARGET_TYPE", targetType.getSimpleName());
    }

    public static Message ILLEGAL_VALUE(Class<?> enumClass, String actualValue) {
        return of("ILLEGAL_VALUE", enumerators(enumClass), actualValue);
    }

    public static Message UNKNOWN_PROPERTY(String name) {
        return of("UNKNOWN_PROPERTY", name);
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

    private Messages() {
    }
}
