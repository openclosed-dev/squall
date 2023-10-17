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
import java.util.spi.ToolProvider;

import dev.openclosed.squall.cli.spi.MessageBundle;
import dev.openclosed.squall.cli.spi.Subcommand;
import dev.openclosed.squall.cli.spi.SubcommandProvider;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExecutionException;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Help;
import picocli.CommandLine.IDefaultValueProvider;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.IExecutionStrategy;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParseResult;
import picocli.CommandLine.RunLast;
import picocli.CommandLine.ScopeType;

@Command(
    name = "squall",
    description = "Squall is a document generator for SQL Data Definition Language (DDL).",
    version = {
        "Squall ${program.version}",
        "JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
        "OS: ${os.name} ${os.version} ${os.arch}"
    }
)
public final class RootCommand implements
    ToolProvider,
    LoggerProvider,
    IFactory,
    IDefaultValueProvider,
    IExecutionExceptionHandler {

    private static final String BUNDLE_BASE_NAME = "dev.openclosed.squall.cli.HelpMessages";

    private final String defaultWorkDir;

    @SuppressWarnings("unused")
    private PrintWriter out;
    private PrintWriter err;

    private System.Logger logger;

    private MessageBundle messageBundle;

    private Exception executionException;

    public RootCommand() {
        this(System.getProperty("user.dir"));
    }

    public RootCommand(Path workDir) {
        this(workDir.toString());
    }

    public RootCommand(String workDir) {
        this.defaultWorkDir = workDir;
    }

    @Override
    public String name() {
        return "squall";
    }

    @Override
    public int run(PrintWriter out, PrintWriter err, String... args) {
        return executeCommand(out, err, args);
    }

    // LoggerProvider

    @Override
    public Logger getLogger() {
        if (this.logger == null) {
            throw new IllegalStateException("logger is not created yet");
        }
        return this.logger;
    }

    // IFactory

    @Override
    @SuppressWarnings("unchecked")
    public <K> K create(Class<K> clazz) throws Exception {
        if (clazz == Subcommand.ExecutionContext.class) {
            return (K) new ExecutionContext(this, this.messageBundle);
        }
        return CommandLine.defaultFactory().create(clazz);
    }

    // IDefaultValueProvider

    @Override
    public String defaultValue(CommandLine.Model.ArgSpec argSpec) {
        if (argSpec instanceof CommandLine.Model.OptionSpec optionSpec) {
            if ("--directory".equals(optionSpec.longestName())) {
                return defaultWorkDir;
            }
        }
        return null;
    }

    // IExecutionExceptionHandler

    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) {
        this.executionException = ex;
        return ExitCode.SOFTWARE;
    }

    //

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

    // Global options

    @Option(
        names = {"-h", "--help"},
        description = "Show this help message and exit.",
        usageHelp = true,
        scope = ScopeType.INHERIT
    )
    public void requestUsageHelp(boolean requested) {
    }

    @Option(
        names = {"-v", "--version"},
        description = "Print version information and exit.",
        versionHelp = true
    )
    public void requestVersionHelp(boolean requested) {
    }

    @Option(
        names = {"--verbose"},
        description = "Produce detailed output.",
        defaultValue = "false",
        scope = ScopeType.INHERIT
    )
    public void setVerbose(boolean verbose) {
        var level = verbose ? Level.TRACE : Level.INFO;
        this.logger = new DefaultLogger(level, this.err);
    }

    // The entry point

    public static void main(String[] args) {
        new RootCommand().run(args);
    }

    //

    private int executeCommand(PrintWriter out, PrintWriter err, String[] args) {
        this.messageBundle = MessageBundle.forLocale(Locale.getDefault());

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
        var commandLine = new CommandLine(this)
            .setResourceBundle(getResourceBundle())
            .setExecutionStrategy(new CommandExecutor())
            .setExecutionExceptionHandler(this);

        return findSubcommands(commandLine)
            .setOut(out)
            .setErr(err)
            .setDefaultValueProvider(this);
    }

    private CommandLine findSubcommands(CommandLine commandLine) {
        for (var provider : ServiceLoader.load(SubcommandProvider.class)) {
            var child = new CommandLine(provider, this);
            commandLine.addSubcommand(child);
        }
        return commandLine;
    }

    private MessageBundle messages() {
        return this.messageBundle;
    }

    private static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(
            BUNDLE_BASE_NAME,
            Locale.getDefault(),
            RootCommand.class.getClassLoader()
        );
    }

    /**
     * An executor of commands.
     */
    class CommandExecutor implements IExecutionStrategy {

        @Override
        public int execute(ParseResult parseResult) throws ExecutionException {
            final String commandName = getCommandNameToExecute(parseResult);
            final long startTime = System.currentTimeMillis();
            try {
                int result = executeLastCommand(parseResult);
                final long timeElapsed = System.currentTimeMillis() - startTime;
                if (!checkHelpRequested(parseResult)) {
                    getLogger().log(Level.INFO, messages().COMMAND_COMPLETED(commandName, timeElapsed));
                }
                return result;
            } catch (ExecutionException e) {
                reportError(e.getCause());
                throw e;
            }
        }

        private int executeLastCommand(ParseResult parseResult) throws ExecutionException {
            return new RunLast().execute(parseResult);
        }

        private String getCommandNameToExecute(ParseResult parseResult) {
            return getLastParseResult(parseResult).commandSpec().name();
        }

        private ParseResult getLastParseResult(ParseResult parseResult) {
            while (parseResult.hasSubcommand()) {
                parseResult = parseResult.subcommand();
            }
            return parseResult;
        }

        private void reportError(Throwable e) {
            getLogger().log(Level.ERROR, e.getMessage());
            if (e.getCause() != null) {
                getLogger().log(Level.TRACE, e.getCause());
            }
        }

        private boolean checkHelpRequested(ParseResult parseResult) {
            while (parseResult != null) {
                if (parseResult.isUsageHelpRequested() || parseResult.isVersionHelpRequested()) {
                    return true;
                }
                parseResult = parseResult.subcommand();
            }
            return false;
        }
    }
}
