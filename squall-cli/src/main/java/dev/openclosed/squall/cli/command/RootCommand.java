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
import java.lang.System.Logger.Level;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.spi.ToolProvider;

import dev.openclosed.squall.cli.base.LoggerFactory;
import dev.openclosed.squall.cli.spi.BaseCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Help;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

@Command(
        name = "squall",
        description = "Squall is a document generator for SQL Data Definition Language (DDL).",
        mixinStandardHelpOptions = true,
        version = {
                "Squall ${program.version}",
                "Picocli " + picocli.CommandLine.VERSION,
                "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "OS: ${os.name} ${os.version} ${os.arch}"
        }
        )
public final class RootCommand implements ToolProvider, IExecutionExceptionHandler, LoggerFactory {

    private static final String BUNDLE_BASE_NAME = "dev.openclosed.squall.cli.HelpMessages";

    private final String workDir;

    @SuppressWarnings("unused")
    private PrintWriter out;
    private PrintWriter err;

    private Exception executionException;

    public RootCommand() {
        this.workDir = System.getProperty("user.dir");
    }

    public RootCommand(Path workDir) {
        this.workDir = workDir.toString();
    }

    @Override
    public String name() {
        return "squall";
    }

    @Override
    public int run(PrintWriter out, PrintWriter err, String... args) {
        return executeCommand(out, err, args);
    }

    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) {
        this.executionException = ex;
        return ExitCode.SOFTWARE;
    }

    @Override
    public Logger createLogger(Level level) {
        return new DefaultLogger(level, this.err);
    }

    public int run(String... args) {
        var charset = StandardCharsets.UTF_8;
        return run(
                new PrintWriter(System.out, true, charset),
                new PrintWriter(System.err, true, charset),
                args);
    }

    public Optional<Exception> getExecutionException() {
        return Optional.ofNullable(this.executionException);
    }

    public static void main(String[] args) {
        new RootCommand().run(args);
    }

    private int executeCommand(PrintWriter out, PrintWriter err, String[] args) {
        this.out = out;
        this.err = err;
        var commandLine = createCommandLine(out, err);
        int exitCode = ExitCode.OK;
        if (args.length > 0) {
            exitCode = commandLine.execute(args);
        } else {
            commandLine.usage(out, Help.Ansi.AUTO);
        }
        flushWriters(out, err);
        return exitCode;
    }

    private static void flushWriters(PrintWriter... writers) {
        for (var writer : writers) {
            writer.flush();
        }
    }

    private CommandLine createCommandLine(PrintWriter out, PrintWriter err) {
        var commandLine = new CommandLine(this, new ObjectFactory())
            .setResourceBundle(getResourceBundle())
            .addSubcommand(new HelpCommand());

        ServiceLoader.load(BaseCommand.class).stream()
            .map(Provider::type)
            .filter(t -> t.isAnnotationPresent(Command.class))
            .forEach(commandLine::addSubcommand);

        commandLine.setDefaultValueProvider(new DefaultValueProvider(this.workDir))
                .setExecutionExceptionHandler(this)
                .setOut(out)
                .setErr(err);

        return commandLine;
    }

    private static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(
            BUNDLE_BASE_NAME,
            Locale.getDefault(),
            RootCommand.class.getClassLoader()
        );
    }
}
