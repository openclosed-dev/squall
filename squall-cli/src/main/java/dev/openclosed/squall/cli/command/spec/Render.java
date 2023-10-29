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

package dev.openclosed.squall.cli.command.spec;

import dev.openclosed.squall.api.parser.CommentProcessor;
import dev.openclosed.squall.api.text.Problem;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.cli.spi.CommandException;
import dev.openclosed.squall.cli.spi.Subcommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Command(
    name = "render",
    description = "Generate specification documents from SQL sources."
)
final class Render implements Subcommand {

    @Mixin
    private ExecutionContext context;

    @Parameters(
        paramLabel = "NAME",
        description = {
            "Name of renderer defined in the configuration.",
            "When no name is specified, \"default\" will be selected."
        },
        defaultValue = "default"
    )
    private String[] names;

    @Override
    public ExecutionContext context() {
        return this.context;
    }

    @Override
    public void runWithConfig(RootConfig config) {
        if (config.sources().isEmpty()) {
            logger().log(System.Logger.Level.INFO, messages().NO_SQL_SOURCES());
        } else {
            processAll(names, config);
        }
    }

    private void processAll(String[] names, RootConfig config) {
        Map<String, RenderConfig> renderConfigs = selectRenderers(config, names);
        var spec = readDatabaseSpec(config);
        for (var entry : renderConfigs.entrySet()) {
            renderDocument(spec, entry.getKey(), entry.getValue(), config);
        }
    }

    private DatabaseSpec readDatabaseSpec(RootConfig config) {
        var specBuilder = DatabaseSpec.builder();
        config.metadata().ifPresent(specBuilder::setMetadata);
        parseSqlSources(config.sources(), config.parser(), specBuilder);
        return specBuilder.build();
    }

    private void parseSqlSources(
        List<String> sources,
        ParserConfig parserConfig,
        DatabaseSpec.Builder specBuilder) {
        var parserFactory = SqlParserFactory.newInstance(parserConfig.dialect());
        var parser = parserFactory.createParser(parserConfig,
            specBuilder,
            CommentProcessor.newDocCommentProcessor());
        int failures = 0;
        for (String source : sources) {
            Path fullPath = resolvePath(source);
            try {
                logger().log(System.Logger.Level.INFO, messages().PARSING_SQL_SOURCE(source));
                String sql = Files.readString(fullPath);
                int errors = parser.parse(sql);
                reportSqlProblems(parser.getProblems());
                if (errors == 0) {
                    logger().log(System.Logger.Level.INFO, messages().PARSED_SQL_SOURCE(source));
                } else {
                    failures++;
                }
            } catch (NoSuchFileException e) {
                logger().log(System.Logger.Level.ERROR, messages().SQL_FILE_NOT_EXIST(fullPath));
                failures++;
            } catch (IOException e) {
                logger().log(System.Logger.Level.ERROR, messages().FAILED_TO_READ_FILE(fullPath));
                failures++;
            }
        }
        if (failures > 0) {
            throw new CommandException(messages().FOUND_SQL_ERRORS(failures));
        }
    }

    private void reportSqlProblems(List<Problem> problems) {
        var logger = logger();
        for (var problem : problems) {
            logger.log(problem.severity(), problem.toString());
        }
    }

    private Map<String, RenderConfig> selectRenderers(RootConfig rootConfig, String[] names) {
        if (names == null || names.length == 0) {
            names = new String[] {"default"};
        }

        var renderers = rootConfig.renderers();
        if (renderers.isEmpty()) {
            throw new CommandException(messages().NO_RENDERER_DEFINED());
        }

        return Stream.of(names)
            .collect(Collectors.toMap(Function.identity(),
                name -> {
                    if (renderers.containsKey(name)) {
                        return renderers.get(name);
                    } else {
                        throw new CommandException(messages().RENDERER_NOT_DEFINED(name));
                    }
                }
            ));
    }

    private void renderDocument(
        DatabaseSpec spec, String name, RenderConfig renderConfig,
        RootConfig rootConfig) {
        var factory = getRendererFactory(renderConfig.format());
        var renderer = factory.createRenderer(renderConfig);
        Path outDir = requireOutputDirectory(rootConfig.outDir());
        try  {
            logger().log(System.Logger.Level.INFO, messages().RENDERING_SPEC(name, outDir));
            renderer.render(spec, outDir);
            logger().log(System.Logger.Level.INFO, messages().RENDERED_SPEC(name, outDir));
        } catch (IOException e) {
            throw new CommandException(messages().FAILED_TO_WRITE_FILE(outDir), e);
        }
    }

    private RendererFactory getRendererFactory(String format) {
        try {
            return RendererFactory.newInstance(format);
        } catch (Exception e) {
            throw new CommandException(messages().RENDERER_UNAVAILABLE(format), e);
        }
    }

    private Path requireOutputDirectory(String outDir) {
        Path path = resolvePath(outDir);
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new CommandException(messages().CANNOT_TO_CREATE_OUTPUT_DIRECTORY(path), e);
        }
    }
}
