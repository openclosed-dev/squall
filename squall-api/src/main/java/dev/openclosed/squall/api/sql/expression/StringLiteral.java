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

import java.util.Objects;

/**
 * Literal expression of string type.
 * @param value the string value.
 */
public record StringLiteral(String value) implements Literal {

    private static final StringLiteral EMPTY = new StringLiteral("");

    /**
     * Creates a string literal of the specified value.
     * @param value the string value.
     * @return created literal with the specified value.
     */
    public static StringLiteral of(String value) {
        Objects.requireNonNull(value);
        if (value.isEmpty()) {
            return EMPTY;
        } else {
            return new StringLiteral(value);
        }
    }

    /**
     *
     Creates an instance of a {@code StringLiteral} record class.
     * @param value the string value.
     */
    public StringLiteral {
        Objects.requireNonNull(value);
    }

    @Override
    public Type type() {
        return Type.STRING;
    }

    @Override
    public String toSql() {
        return new StringBuilder()
            .append('\'').append(value()).append('\'')
            .toString();
    }
}
