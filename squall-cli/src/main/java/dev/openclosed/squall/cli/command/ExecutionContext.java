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

package dev.openclosed.squall.cli.command;

import dev.openclosed.squall.api.text.Problem;
import dev.openclosed.squall.api.config.ConfigLoader;
import dev.openclosed.squall.api.config.ConfigurationException;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.cli.spi.CommandException;
import dev.openclosed.squall.cli.spi.MessageBundle;
import dev.openclosed.squall.cli.spi.Subcommand;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

final class ExecutionContext implements Subcommand.ExecutionContext {

    private final LoggerProvider loggerProvider;
    private final MessageBundle messageBundle;
    private System.Logger logger;

    private Path directory;
    private Path file;

    ExecutionContext(LoggerProvider loggerProvider, MessageBundle messageBundle) {
        this.loggerProvider = loggerProvider;
        this.messageBundle = messageBundle;
    }

    // ExecutionContext

    @Override
    public System.Logger logger() {
        if (this.logger == null) {
            this.logger = this.loggerProvider.getLogger();
        }
        return this.logger;
    }

    @Override
    public MessageBundle messages() {
        return this.messageBundle;
    }

    @Override
    public Path workDirectory() {
        return this.directory;
    }

    @Override
    public Path configFile() {
        return this.file;
    }

    @Override
    public Path checkDirectoryExists() {
        Path path = workDirectory();
        if (!Files.exists(path)) {
            throw new CommandException(messages().WORKING_DIRECTORY_NOT_EXIST(path));
        } else if (!Files.isDirectory(path)) {
            throw new CommandException(messages().WORKING_DIRECTORY_IS_NOT_DIRECTORY(path));
        }
        return path;
    }

    @Override
    public RootConfig loadConfiguration() {
        Path dir = workDirectory();
        Path configFile = configFile();
        Path configPath = dir.resolve(configFile);
        var configLoader = ConfigLoader.newLoader();
        try {
            logger().log(System.Logger.Level.INFO, messages().LOADING_CONFIGURATION(configFile));
            String json = Files.readString(configPath);
            var config = configLoader.loadFromJson(json);
            reportConfigurationProblems(configLoader.getProblems());
            logger().log(System.Logger.Level.INFO, messages().LOADED_CONFIGURATION());
            return config;
        } catch (NoSuchFileException e) {
            throw new CommandException(messages().CONFIGURATION_NOT_EXIST(configPath));
        } catch (IOException e) {
            throw new CommandException(messages().FAILED_TO_READ_FILE(configPath));
        } catch (ConfigurationException e) {
            reportConfigurationProblems(e.getProblems());
            throw new CommandException(messages().CONFIGURATION_INVALID(), e);
        }
    }

    // Options

    @Option(names = { "-C", "--directory" },
        paramLabel = "DIR",
        description  = "Change to directory DIR.",
        defaultValue = "${user.dir}"
    )
    public void setDirectory(String directory) {
        this.directory = Path.of(directory);
    }

    @Option(names = { "-f", "--file" },
        paramLabel = "FILE",
        description  = "Path to the configuration file.",
        defaultValue = Subcommand.DEFAULT_CONFIG_FILENAME
    )
    public void setFile(String file) {
        this.file = Path.of(file);
    }

    private void reportConfigurationProblems(List<Problem> problems) {
        var log = logger();
        for (var problem : problems) {
            log.log(problem.severity(), problem.toString());
        }
    }
}
