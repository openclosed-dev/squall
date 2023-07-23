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
 * An exception thrown if configuration is invalid.
 */
public final class ConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -3054672590244440349L;

    private final List<Problem> problems;

    public ConfigurationException(String message, List<Problem> problems) {
        super(message);
        this.problems = problems;
    }

    public ConfigurationException(Message message, List<Problem> problems) {
        this(message.get(), problems);
    }

    /**
     * Returns the found problem.
     * @return found problem.
     */
    public List<Problem> getProblems() {
        return problems;
    }
}
