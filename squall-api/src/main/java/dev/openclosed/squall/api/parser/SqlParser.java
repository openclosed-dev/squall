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

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.expression.Expression;

import java.util.List;

/**
 * SQL parser.
 */
public interface SqlParser {

    /**
     * Parses SQL text.
     * @param text the text to parse.
     * @return the number of errors detected.
     */
    int parse(CharSequence text);

    /**
     * Parses SQL expression text.
     * @param text the text to parse.
     * @return the parsed expression.
     * @throws SqlSyntaxException if a syntax error was detected in the expression.
     */
    Expression parseExpression(CharSequence text) throws SqlSyntaxException;

    /**
     * Returns the problems detected by this parser.
     * @return immutable list of problems.
     */
    List<Problem> getProblems();
}
