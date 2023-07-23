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

package dev.openclosed.squall.cli.command.sub;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import dev.openclosed.squall.cli.base.Messages;
import dev.openclosed.squall.cli.spi.BaseCommand;
import dev.openclosed.squall.cli.spi.CommandException;
import dev.openclosed.squall.cli.spi.ConfigurableCommand;
import picocli.CommandLine.Command;

@Command(
        name = "init",
        description = "Generate initial configuration."
        )
public final class Init extends BaseCommand {

    @Override
    public void runCommand() {
        generateConfigurationFile(getDirectory());
        getLogger().log(Level.INFO, Messages.INITIALIZED_SUCCESSFULLY(getDirectory()));
    }

    private void generateConfigurationFile(Path dir) {
        Path path = dir.resolve(ConfigurableCommand.DEFAULT_CONFIG_FILENAME);
        try (var in = openConfigurationResource()) {
            try (var out = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW)) {
                in.transferTo(out);
            }
        } catch (FileAlreadyExistsException e) {
            throw new CommandException(Messages.CONFIGURATION_ALREADY_EXISTS(dir));
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
