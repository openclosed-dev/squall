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

package dev.openclosed.squall.parser.basic;

/**
 * SQL keywords.
 */
public interface Keyword extends Token {

    String canonicalName();

    /**
     * Returns the corresponding standard keyword.
     * @return standard keyword
     */
    StandardKeyword standard();

    // Token interface

    @Override
    default TokenType type() {
        return TokenType.KEYWORD;
    }

    @Override
    default String text() {
        return canonicalName();
    }

    @Override
    default Object value() {
        return this;
    }

    @Override
    default String toIdentifier() {
        return canonicalName().toLowerCase();
    }

    @Override
    default boolean isKeyword() {
        return true;
    }
}
