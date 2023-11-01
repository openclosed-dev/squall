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

/**
 * Defines the Squall API and its default implementation.
 */
module dev.openclosed.squall.api {

    exports dev.openclosed.squall.api;
    exports dev.openclosed.squall.api.text;
    exports dev.openclosed.squall.api.config;
    exports dev.openclosed.squall.api.parser;
    exports dev.openclosed.squall.api.renderer;
    exports dev.openclosed.squall.api.sql.spec;
    exports dev.openclosed.squall.api.sql.datatype;
    exports dev.openclosed.squall.api.sql.expression;
    exports dev.openclosed.squall.api.util;
    exports dev.openclosed.squall.api.text.json;
    exports dev.openclosed.squall.api.message.spi;
    exports dev.openclosed.squall.api.message;
    exports dev.openclosed.squall.api.sql.annotation;

    uses dev.openclosed.squall.api.message.spi.MessagesProvider;
    uses dev.openclosed.squall.api.parser.SqlParserFactory;
    uses dev.openclosed.squall.api.renderer.RendererFactory;
    uses dev.openclosed.squall.api.sql.annotation.DocAnnotationFactory;
    uses dev.openclosed.squall.api.text.json.JsonReader;
    uses dev.openclosed.squall.api.text.json.JsonWriter;

    provides dev.openclosed.squall.api.message.spi.MessagesProvider
        with dev.openclosed.squall.api.message.spi.MessagesProvider;
}
