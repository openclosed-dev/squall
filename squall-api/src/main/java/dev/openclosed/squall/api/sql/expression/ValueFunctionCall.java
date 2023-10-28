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
 * Special function call to retrieve value of a system variable.
 * @param name the name of the system variable.
 */
public record ValueFunctionCall(String name) implements FunctionCall {

    /**
     * Creates an instance of a {@code ValueFunctionCall} record class.
     * @param name the name of the system variable.
     */
    public ValueFunctionCall {
        Objects.requireNonNull(name);
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toSql() {
        return name();
    }
}
