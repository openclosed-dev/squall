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
import java.util.Objects;
import java.util.stream.Collectors;

record SequenceFunctionCall(
    Expression.Type type,
    String name,
    List<Expression> arguments,
    List<String> sequenceName)
    implements dev.openclosed.squall.api.sql.expression.SequenceFunctionCall, MapSourceExpression {

    SequenceFunctionCall {
        Objects.requireNonNull(type);
        Objects.requireNonNull(name);
        Objects.requireNonNull(arguments);
        Objects.requireNonNull(sequenceName);
    }

    @Override
    public String toSql() {
        return FunctionCall.toSql(name(), arguments());
    }

    private String qualifiedSequenceName() {
        return sequenceName().stream()
            .filter(n -> !n.isEmpty())
            .collect(Collectors.joining("."));
    }
}
