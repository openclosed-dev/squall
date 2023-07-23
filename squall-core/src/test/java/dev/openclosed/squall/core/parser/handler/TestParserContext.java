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

package dev.openclosed.squall.core.parser.handler;

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.ParserContext;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;

class TestParserContext implements ParserContext {
    private final ParserConfig config;
    private final DatabaseSpecBuilder builder;

    TestParserContext() {
        this.config = ParserConfig.getDefault();
        this.builder = DatabaseSpecBuilder.newBuilder();
    }

    @Override
    public ParserConfig config() {
        return config;
    }

    @Override
    public DatabaseSpecBuilder builder() {
        return builder;
    }

    @Override
    public void reportProblem(System.Logger.Level severity, Message message, Location location) {
        // Do nothing
    }
}
