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

import dev.openclosed.squall.api.sql.spec.DatabaseSpec;

module dev.openclosed.squall.api {

    exports dev.openclosed.squall.api.base;
    exports dev.openclosed.squall.api.config;
    exports dev.openclosed.squall.api.parser;
    exports dev.openclosed.squall.api.renderer;
    exports dev.openclosed.squall.api.spi;
    exports dev.openclosed.squall.api.sql.spec;
    exports dev.openclosed.squall.api.sql.datatype;
    exports dev.openclosed.squall.api.sql.expression;
    exports dev.openclosed.squall.api.util;

    uses dev.openclosed.squall.api.config.ConfigLoader;
    uses dev.openclosed.squall.api.parser.SqlParserFactory;
    uses dev.openclosed.squall.api.renderer.RendererFactory;
    uses DatabaseSpec.Builder;
    uses dev.openclosed.squall.api.spi.JsonReader;
    uses dev.openclosed.squall.api.spi.JsonWriter;
    uses dev.openclosed.squall.api.spi.MessagesProvider;
}
