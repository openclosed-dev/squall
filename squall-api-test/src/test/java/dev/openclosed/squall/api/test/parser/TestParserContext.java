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

package dev.openclosed.squall.api.test.parser;

import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.message.Message;
import dev.openclosed.squall.api.text.Problem;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.ParserContext;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.annotation.DocAnnotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class TestParserContext implements ParserContext {
    private final ParserConfig config;
    private final DatabaseSpec.Builder builder;
    private final List<DocAnnotation<?>> annotations = new ArrayList<>();
    private final List<Problem> problems = new ArrayList<>();

    TestParserContext() {
        this.config = ParserConfig.DEFAULT;
        this.builder = DatabaseSpec.builder();
    }

    @Override
    public ParserConfig config() {
        return config;
    }

    @Override
    public void addAnnotations(List<DocAnnotation<?>> annotations) {
        this.annotations.addAll(annotations);
    }

    @Override
    public void reportProblem(System.Logger.Level severity, Message message, Location location) {
        this.problems.add(new ReportedProblem(severity, message, Optional.of(location)));
    }

    List<DocAnnotation<?>> getAnnotations() {
        return annotations;
    }

    List<Problem> getProblems() {
        return this.problems;
    }

    record ReportedProblem(System.Logger.Level severity, Message message, Optional<Location> location)
        implements Problem {
    }
}
