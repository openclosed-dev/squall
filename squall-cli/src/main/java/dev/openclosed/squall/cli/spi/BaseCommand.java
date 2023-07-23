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

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.openclosed.squall.cli.base.LoggerFactory;
import dev.openclosed.squall.cli.base.Messages;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

public abstract class BaseCommand implements Runnable {

    @Option(names = { "-C", "--directory" },
            paramLabel = "DIR",
            description  = "change to directory DIR",
            defaultValue = "${user.dir}"
            )
    private Path directory;

    @Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "display this help message")
    private boolean usageHelpRequested;

    @Option(names = {"--verbose"})
    private boolean verbose;

    @ParentCommand
    private LoggerFactory loggerFactory;

    private Logger logger;

    public final Logger getLogger() {
        return logger;
    }

    public final Path getDirectory() {
        return directory;
    }

    /**
     * Returns the name of this command.
     * @return the name of this command.
     */
    public String getName() {
        var annotation = getClass().getAnnotation(CommandLine.Command.class);
        return annotation.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void run() {
        final long startTime = System.currentTimeMillis();
        this.logger = createLogger();
        try {
            prepareCommand();
            runCommand();
            final long timeElapsed = System.currentTimeMillis() - startTime;
            getLogger().log(Level.INFO, Messages.COMMAND_COMPLETED(getName(), timeElapsed));
        } catch (CommandException e) {
            reportError(e);
            throw e;
        } catch (Exception e) {
            reportError(e);
            throw e;
        }
    }

    private Logger createLogger() {
        return this.loggerFactory.createLogger(determineLogLevel());
    }

    private Level determineLogLevel() {
        if (this.verbose) {
            return Level.TRACE;
        }
        return Level.INFO;
    }

    /**
     * Prepares this command.
     * @throws CommandException if failed while preparing this command.
     */
    protected void prepareCommand() {
        validateWorkingDirectory();
    }

    private void validateWorkingDirectory() {
        var path = getDirectory();
        if (!Files.exists(path)) {
            throw new CommandException(Messages.WORKING_DIRECTORY_NOT_EXIST(path));
        } else if (!Files.isDirectory(directory)) {
            throw new CommandException(Messages.WORKING_DIRECTORY_IS_NOT_DIRECTORY(path));
        }
    }

    private void reportError(Exception e) {
        getLogger().log(Level.ERROR, e.getMessage());
        if (e.getCause() != null) {
            getLogger().log(Level.TRACE, e.getCause());
        }
    }

    /**
     * Runs this command.
     * @throws CommandException if failed while running this command.
     */
    protected abstract void runCommand();
}
