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

import dev.openclosed.squall.core.parser.postgresql.PostgreSqlParserFactory;

module dev.openclosed.squall.core {

    requires dev.openclosed.squall.api;

    uses dev.openclosed.squall.core.spi.MessagesProvider;

    provides dev.openclosed.squall.api.config.ConfigLoader
        with dev.openclosed.squall.core.config.DefaultConfigLoader;
    provides dev.openclosed.squall.api.parser.SqlParserFactory
        with PostgreSqlParserFactory;
    provides dev.openclosed.squall.api.parser.DocCommentHandler
        with dev.openclosed.squall.core.parser.handler.DefaultDocCommentHandler;
    provides dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder
        with dev.openclosed.squall.core.spec.builder.DefaultDatabaseSpecBuilder;
    provides dev.openclosed.squall.core.spi.MessagesProvider
        with dev.openclosed.squall.core.spi.impl.DefaultMessagesProvider;
    provides dev.openclosed.squall.api.spi.RendererMessagesProvider
        with dev.openclosed.squall.core.base.DefaultRendererMessagesProvider;
}
