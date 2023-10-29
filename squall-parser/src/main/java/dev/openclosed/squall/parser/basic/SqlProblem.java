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

package dev.openclosed.squall.parser.basic;

import java.lang.System.Logger.Level;
import java.util.Optional;

import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.message.Message;
import dev.openclosed.squall.api.text.Problem;

record SqlProblem(
        Level severity,
        Message message,
        Optional<Location> location,
        Optional<String> source) implements Problem {

    SqlProblem(Level severity, Message message, Location location, Optional<String> source) {
        this(severity, message, Optional.of(location), source);
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
        source.ifPresent(value -> builder.append("\n").append(value));
        return builder.toString();
    }
}
