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

import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 * Default implementation of {@link Logger}.
 */
final class DefaultLogger implements Logger {

    private final Level minLevel;
    private final PrintWriter writer;

    DefaultLogger(Level minLevel, PrintWriter writer) {
        this.minLevel = minLevel;
        this.writer = writer;
    }

    @Override
    public String getName() {
        return DefaultLogger.class.getName();
    }

    @Override
    public boolean isLoggable(Level level) {
        return level.getSeverity() >= this.minLevel.getSeverity();
    }

    @Override
    public void log(Level level, String msg) {
        if (!isLoggable(level)) {
            return;
        }
        switch (level) {
        case INFO -> println(msg);
        case WARNING -> println("WARN: " + msg);
        case ERROR -> println("ERROR: " + msg);
        default -> println(msg);
        }
    }

    @Override
    public void log(Level level, Supplier<String> msgSupplier) {
        log(level, msgSupplier.get());
    }

    @Override
    public void log(Level level, Object obj) {
        log(level, obj.toString());
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        throw new UnsupportedOperationException();
    }

    private void println(String msg) {
        this.writer.println(msg);
    }
}
