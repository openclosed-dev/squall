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

package dev.openclosed.squall.api.sql.expression;

/**
 * Expression in SQL.
 */
public interface Expression {

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
        /** Calling of a function. */
        FUNCTION,
        /** Calling of sequence manipulation function. */
        SEQUENCE_FUNCTION,
        /** Unary operator. */
        UNARY_OPERATOR,
        /** Binary operator. */
        BINARY_OPERATOR,
        /** Column reference. */
        COLUMN_REFERENCE,
        /** Typecasting. */
        TYPECAST,
        /** Is predicate. */
        IS,
        /** In comparison. */
        IN,
        /** Not In comparison. */
        NOT_IN,
        /** Case expression. */
        CASE,
        /** When clause. */
        WHEN
    }

    /**
     * Returns the type of this expression.
     * @return the type of this expression.
     */
    Type type();

    /**
     * Returns whether this expression is complex or not.
     * @return {@code true} if it may be hard to read without outermost parentheses.
     */
    default boolean isComplex() {
        return false;
    }

    /**
     * Represents this expression in SQL notation.
     * @return the string representation in SQL notation.
     */
    String toSql();

    /**
     * NULL constant expression.
     */
    Expression NULL = new Null();
}
