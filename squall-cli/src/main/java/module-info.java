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
 * Provides command line interface.
 */
open module dev.openclosed.squall.cli {
    requires transitive dev.openclosed.squall.api;
    requires info.picocli;

    // the only package to be exported
    exports dev.openclosed.squall.cli.spi;

    uses dev.openclosed.squall.cli.spi.SubcommandProvider;
    uses dev.openclosed.squall.cli.spi.MessagesProvider;

    provides dev.openclosed.squall.cli.spi.SubcommandProvider
        with dev.openclosed.squall.cli.command.config.ConfigCommandProvider,
            dev.openclosed.squall.cli.command.spec.SpecCommandProvider;

    provides dev.openclosed.squall.cli.spi.HelpMessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;

    provides dev.openclosed.squall.cli.spi.MessagesProvider
        with dev.openclosed.squall.cli.spi.impl.DefaultResourceBundleProvider;
}
