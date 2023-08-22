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

import dev.openclosed.squall.api.config.RootConfig;

import java.nio.file.Path;

/**
 * A subcommand.
 */
public interface Subcommand extends Runnable {

    String DEFAULT_CONFIG_FILENAME = "squall.json";

    /**
     * Returns the execution context.
     * @return the context in which this subcommand is executed.
     */
    ExecutionContext getContext();

    default System.Logger getLogger() {
        return getContext().getLogger();
    }

    default Path getDirectory() {
        return getContext().getDirectory();
    }

    default Path getFile() {
        return getContext().getFile();
    }

    default Path getResolvedFile() {
        return getDirectory().resolve(getFile());
    }

    default Path resolvePath(String other) {
        return getDirectory().resolve(other);
    }

    default Path checkDirectoryExists() {
        return getContext().checkDirectoryExists();
    }

    default RootConfig loadConfiguration() {
        return getContext().loadConfiguration();
    }

    /**
     * Run the command.
     */
    @Override
    default void run() {
        checkDirectoryExists();
        runWithConfig(loadConfiguration());
    }

    /**
     * Runs the command with loaded configuration.
     * @param config the configuration.
     */
    void runWithConfig(RootConfig config);

    /**
     * A context in which the command is executed.
     */
    interface ExecutionContext {

        System.Logger getLogger();

        /**
         * Returns the current working directory.
         * @return the current working directory.
         */
        Path getDirectory();

        Path getFile();

        Path checkDirectoryExists();

        RootConfig loadConfiguration();
    }
}
