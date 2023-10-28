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
 * Literal expression of bit string type.
 * @param value the bit string value.
 */
public record BitStringLiteral(String value) implements Literal {

    private static final BitStringLiteral EMPTY = new BitStringLiteral("");

    /**
     * Creates a bit string of the specified value.
     * @param value the bit string value.
     * @return created bit string of the specified value.
     */
    public static BitStringLiteral of(String value) {
        Objects.requireNonNull(value);
        if (value.isEmpty()) {
            return EMPTY;
        } else {
            return new BitStringLiteral(value);
        }
    }

    /**
     * Creates an instance of a {@code BitStringLiteral} record class.
     * @param value the bit string value.
     */
    public BitStringLiteral {
        Objects.requireNonNull(value);
    }

    @Override
    public Type type() {
        return Type.BIT_STRING;
    }

    @Override
    public String toSql() {
        return new StringBuilder()
            .append("b'").append(value()).append('\'')
            .toString();
    }
}
