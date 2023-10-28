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

package dev.openclosed.squall.api.config;

import java.util.List;

import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.base.Problem;

/**
 * Exception thrown if there exist errors in configuration.
 */
public final class ConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -3054672590244440349L;

    /**
     * List of the found problems.
     */
    private final List<Problem> problems;

    /**
     * Constructs an exception.
     * @param message the message describing the exception.
     * @param problems the problems found while loading configuration.
     */
    public ConfigurationException(Message message, List<Problem> problems) {
        super(message.get());
        this.problems = problems;
    }

    /**
     * Returns the found problem.
     * @return the problem found while loading configuration.
     */
    public List<Problem> getProblems() {
        return problems;
    }
}
