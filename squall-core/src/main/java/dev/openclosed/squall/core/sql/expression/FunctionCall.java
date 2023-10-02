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

import java.util.List;

record FunctionCall(Expression.Type type, String name, List<Expression> arguments)
    implements dev.openclosed.squall.api.sql.expression.FunctionCall, MapSourceExpression {

    @Override
    public String toSql() {
        return toSql(name(), arguments());
    }

    static String toSql(String name, List<Expression> arguments) {
        var sb = new SqlStringBuilder()
            .append(name)
            .append('(');
        for (int i = 0; i < arguments.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(arguments.get(i));
        }
        return sb.append(')').toString();
    }
}
