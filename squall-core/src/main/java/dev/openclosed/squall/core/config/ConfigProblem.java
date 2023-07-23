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

package dev.openclosed.squall.core.config;

import java.lang.System.Logger.Level;
import java.util.Optional;

import dev.openclosed.squall.api.base.JsonPointer;
import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.base.Problem;

/**
 * A problem in the configuration.
 * @param severity the severity of this problem.
 * @param message the message describing this problem.
 * @param location the location where this problem has occurred.
 * @param pointer the JSON pointer where this problem has occurred.
 * @param source the source code block that caused this problem.
 */
record ConfigProblem(
        Level severity,
        Message message,
        Optional<Location> location,
        Optional<JsonPointer> pointer,
        Optional<String> source) implements Problem {

    ConfigProblem(Level severity, Message message) {
        this(severity, message, Optional.empty(), Optional.empty(), Optional.empty());
    }

    ConfigProblem(Level severity, Message message, Location location, Optional<String> source) {
        this(severity, message, Optional.of(location), Optional.empty(), source);
    }

    ConfigProblem(Level severity, Message message, JsonPointer pointer) {
        this(severity, message, Optional.empty(), Optional.of(pointer), Optional.empty());
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append(message.get());
        location.ifPresent(value -> {
            builder.append(" [")
                .append("line ")
                .append(value.lineNo())
                .append(", column ")
                .append(value.columnNo())
                .append("]");
        });
        pointer.ifPresent(value -> {
            builder.append(" [").append(value.toString()).append("]");
        });
        source.ifPresent(value -> {
            builder.append("\n").append(value);
        });
        return builder.toString();
    }
}
