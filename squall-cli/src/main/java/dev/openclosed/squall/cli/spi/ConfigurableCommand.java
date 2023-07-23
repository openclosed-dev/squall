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

package dev.openclosed.squall.cli.spi;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.config.ConfigurationException;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.cli.base.Messages;
import picocli.CommandLine.Option;

public abstract class ConfigurableCommand extends BaseCommand {

    public static final String DEFAULT_CONFIG_FILENAME = "squall.json";

    @Option(names = { "-f", "--file" },
            paramLabel = "FILE",
            description  = "path to the configuration file",
            defaultValue = DEFAULT_CONFIG_FILENAME
            )
    private Path file;

    public final Path getFile() {
        return file;
    }

    @Override
    public final void runCommand() {
        var config = loadConfiguration(getDirectory());
        runConfiguredCommand(config);
    }

    private RootConfig loadConfiguration(Path dir) {
        Path configFile = getFile();
        Path configPath = dir.resolve(configFile);
        var configLoader = ConfigLoader.get();
        try {
            getLogger().log(Level.INFO, Messages.LOADING_CONFIGURATION(configFile));
            String json = Files.readString(configPath);
            var config = configLoader.loadFromJson(json);
            reportConfigurationProblems(configLoader.getProblems());
            getLogger().log(Level.INFO, Messages.LOADED_CONFIGURATION());
            return config;
        } catch (NoSuchFileException e) {
            throw new CommandException(Messages.CONFIGURATION_NOT_EXIST(configPath));
        } catch (IOException e) {
            throw new CommandException(Messages.FAILED_TO_READ_FILE(configPath));
        } catch (ConfigurationException e) {
            reportConfigurationProblems(e.getProblems());
            throw new CommandException(Messages.CONFIGURATION_INVALID(), e);
        }
    }

    private void reportConfigurationProblems(List<Problem> problems) {
        var logger = getLogger();
        for (var problem : problems) {
            logger.log(problem.severity(), problem.toString());
        }
    }

    /**
     * Runs this command with configuration.
     * @param config the root configuration.
     */
    protected abstract void runConfiguredCommand(RootConfig config);
}
