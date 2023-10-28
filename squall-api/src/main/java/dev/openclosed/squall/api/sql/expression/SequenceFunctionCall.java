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
import java.util.stream.Collectors;

/**
 * Calling of a sequence manipulation function.
 * @param name the name of the function.
 * @param arguments the arguments passed to the function.
 * @param sequenceName the name of the sequence.
 */
public record SequenceFunctionCall(
    String name,
    List<Expression> arguments,
    List<String> sequenceName) implements FunctionCall {

    /**
     * Creates a sequence manipulation function.
     * @param name the name of the function.
     * @param arguments the arguments passed to the function.
     * @param sequenceRef the reference to the sequence.
     * @return newly created function.
     */
    public static SequenceFunctionCall of(String name, List<Expression> arguments, ObjectRef sequenceRef) {
        Objects.requireNonNull(sequenceRef);
        return new SequenceFunctionCall(name, arguments, sequenceRef.toList());
    }

    /**
     * Creates an instance of a {@code SequenceFunctionCall} record class.
     * @param name the name of the function.
     * @param arguments the arguments passed to the function.
     * @param sequenceName the reference to the sequence.
     */
    public SequenceFunctionCall {
        Objects.requireNonNull(name);
        Objects.requireNonNull(arguments);
        Objects.requireNonNull(sequenceName);
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (sequenceName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        arguments = List.copyOf(arguments);
        sequenceName = List.copyOf(sequenceName);
    }

    @Override
    public Type type() {
        return Type.SEQUENCE_FUNCTION;
    }

    @Override
    public String toSql() {
        return BasicFunctionCall.toSql(name(), arguments());
    }

    /**
     * Returns the name of the sequence.
     * @return the name of the sequence.
     */
    public String simpleSequenceName() {
        var sequencedName = sequenceName();
        return sequencedName.get(sequencedName.size() - 1);
    }

    /**
     * Returns the fully qualified name of the sequence.
     * @return the fully qualified name of the sequence.
     */
    public String fullSequenceName() {
        return sequenceName().stream().collect(Collectors.joining("."));
    }
}
