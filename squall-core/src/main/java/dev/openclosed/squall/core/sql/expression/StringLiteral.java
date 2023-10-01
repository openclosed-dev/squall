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

package dev.openclosed.squall.core.sql.expression;

import dev.openclosed.squall.api.sql.expression.Expression;

/**
 * An implementation of string type literal.
 * @param type the type of the expression.
 * @param value the value of the literal.
 */
record StringLiteral(Expression.Type type, String value) implements
    dev.openclosed.squall.api.sql.expression.StringLiteral,
    MapSourceExpression {

    @Override
    public String toSql() {
        return new StringBuilder()
            .append('\'').append(value()).append('\'')
            .toString();
    }
}
