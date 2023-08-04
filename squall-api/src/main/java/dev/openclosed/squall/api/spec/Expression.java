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

package dev.openclosed.squall.api.spec;

import dev.openclosed.squall.api.base.MapSource;

import java.util.Map;

/**
 * An expression.
 */
public interface Expression extends MapSource {

    /**
     * Type of the expression.
     */
    enum Type {
        /** String literal. */
        STRING,
        /** Bit-string literal. */
        BIT_STRING,
        /** Numeric literal. */
        NUMBER,
        /** Boolean literal. */
        BOOLEAN,
        /** NULL literal. */
        NULL,
        FUNCTION,
        UNARY_OPERATOR,
        BINARY_OPERATOR,
        COLUMN_REFERENCE,
        /** Typecasting. */
        TYPECAST,
        /** IS predicate. */
        IS,
        /** IN comparison. */
        IN,
        /** NOT IN comparison. */
        NOT_IN,
        CASE
    }

    /**
     * Returns the type of this expression.
     * @return the type of this expression.
     */
    Type type();

    /**
     * NULL literal.
     */
    Expression NULL = new Expression() {

        private static final Map<String, Object> MAP = Map.of("type", "null");

        @Override
        public Type type() {
            return Type.NULL;
        }

        @Override
        public String toString() {
            return "null";
        }

        @Override
        public Map<String, Object> toMap() {
            return MAP;
        }
    };

    /**
     * Boolean TRUE literal.
     */
    Expression TRUE = new Expression() {

        private static final Map<String, Object> MAP = Map.of(
            "type", "boolean",
            "value", "true");

        @Override
        public Type type() {
            return Type.BOOLEAN;
        }

        @Override
        public String toString() {
            return "true";
        }

        @Override
        public Map<String, Object> toMap() {
            return MAP;
        }
    };

    /**
     * Boolean FALSE literal.
     */
    Expression FALSE = new Expression() {

        private static final Map<String, Object> MAP = Map.of(
            "type", "boolean",
            "value", "false");

        @Override
        public Type type() {
            return Type.BOOLEAN;
        }

        @Override
        public String toString() {
            return "false";
        }

        @Override
        public Map<String, Object> toMap() {
            return MAP;
        }
    };
}
