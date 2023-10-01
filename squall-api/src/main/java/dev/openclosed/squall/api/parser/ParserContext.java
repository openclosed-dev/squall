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

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.sql.spec.DocAnnotation;

import java.util.List;

/**
 * Parsing context.
 */
public interface ParserContext {

    /**
     * Returns the configuration of this parser.
     * @return the configuration of this parser.
     */
    ParserConfig config();

    void addAnnotations(List<DocAnnotation> annotations);

    void reportProblem(System.Logger.Level severity, Message message, Location location);
}
