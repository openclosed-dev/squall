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

package dev.openclosed.squall.cli.command.config;

import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.cli.base.Messages;
import dev.openclosed.squall.cli.spi.CommandException;
import dev.openclosed.squall.cli.spi.Subcommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Command(
    name = "init",
    description = "Generate initial configuration."
)
final class Init implements Subcommand {

    @Mixin
    private ExecutionContext context;

    @Override
    public ExecutionContext getContext() {
        return this.context;
    }

    @Override
    public void run() {
        checkDirectoryExists();
        generateConfigurationFile(getResolvedFile());
        getLogger().log(System.Logger.Level.INFO, Messages.INITIALIZED_SUCCESSFULLY(getDirectory()));
    }

    @Override
    public void runWithConfig(RootConfig config) {
        // Do nothing
    }

    private void generateConfigurationFile(Path path) {
        try (var in = openConfigurationResource()) {
            try (var out = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
                in.transferTo(out);
            }
        } catch (FileAlreadyExistsException e) {
            throw new CommandException(Messages.CONFIGURATION_ALREADY_EXISTS(path));
        } catch (IOException e) {
            throw new CommandException(Messages.FAILED_TO_WRITE_FILE(path), e);
        }
    }

    private InputStream openConfigurationResource() {
        var in = getClass().getResourceAsStream("squall.default.jsonc");
        if (in == null) {
            throw new IllegalStateException("resource not found");
        }
        return in;
    }
}
