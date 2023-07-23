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

package dev.openclosed.squall.core.parser;

/**
 * Type of token.
 */
public enum TokenType {
    /** End of input. */
    EOI,
    /** A SQL keyword. */
    KEYWORD,
    /** An identifier for table, column, or other schema objects. */
    IDENTIFIER,
    /** An quoted identifier. */
    QUOTED_IDENTIFIER,
    /** An symbol. */
    SYMBOL,
    /** A comment on a single line. */
    LINE_COMMENT,
    /** C-style block comment. */
    BLOCK_COMMENT,

    // constants

    /** A string constant. */
    STRING,
    /** A bit-string constant. */
    BIT_STRING,
    /** A numeric constant. */
    NUMBER,
    /** A numeric constant without fractional part. */
    INTEGER;
}
