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

import java.util.List;
import java.util.Objects;

/**
 * Function call with arguments.
 * @param name the name of the function.
 * @param arguments the arguments given to the function.
 */
public record BasicFunctionCall(String name, List<Expression> arguments)
    implements FunctionCall {

    public BasicFunctionCall {
        Objects.requireNonNull(name);
        Objects.requireNonNull(arguments);
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }
        arguments = List.copyOf(arguments);
    }

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
