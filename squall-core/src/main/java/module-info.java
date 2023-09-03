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

import dev.openclosed.squall.core.spec.builder.DatabaseSpecBuilder;

module dev.openclosed.squall.core {

    requires dev.openclosed.squall.api;

    uses dev.openclosed.squall.api.spi.MessagesProvider;

    provides dev.openclosed.squall.api.config.ConfigLoader
        with dev.openclosed.squall.core.config.DefaultConfigLoader;
    provides dev.openclosed.squall.api.spec.DatabaseSpec.Builder
        with DatabaseSpecBuilder;
    provides dev.openclosed.squall.api.spi.MessagesProvider
        with dev.openclosed.squall.core.base.DefaultMessagesProvider;
    provides dev.openclosed.squall.api.spec.ExpressionFactory
        with dev.openclosed.squall.core.spec.expression.DefaultExpressionFactory;
}
