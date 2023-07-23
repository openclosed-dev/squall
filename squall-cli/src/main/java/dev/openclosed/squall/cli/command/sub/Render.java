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
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.config.RootConfig;
import dev.openclosed.squall.api.parser.CommentHandlers;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import dev.openclosed.squall.cli.base.Messages;
import dev.openclosed.squall.cli.spi.CommandException;
import dev.openclosed.squall.cli.spi.ConfigurableCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "render",
        description = "Generate specification documents from SQL sources."
        )
public final class Render extends ConfigurableCommand {

    @Parameters(
            paramLabel = "NAME",
            description = "name of the renderer defined in configuration"
            )
    private String[] names;

    @Override
    protected void runConfiguredCommand(RootConfig config) {
        if (config.sources().isEmpty()) {
            getLogger().log(Level.INFO, Messages.NO_SQL_SOURCES());
        } else {
            processAll(config);
        }
    }

    private void processAll(RootConfig config) {
        Map<String, RenderConfig> renderConfigs = selectRenderers(config, this.names);
        var spec = readDatabaseSpec(config);
        for (var entry : renderConfigs.entrySet()) {
            renderDocument(spec, entry.getKey(), entry.getValue(), config);
        }
    }

    private DatabaseSpec readDatabaseSpec(RootConfig config) {
        var specBuilder = DatabaseSpecBuilder.newBuilder();
        config.title().ifPresent(specBuilder::setTitle);
        parseSqlSources(config.sources(), config.parser(), specBuilder);
        return specBuilder.build();
    }

    private void parseSqlSources(List<String> sources,
            ParserConfig parserConfig,
            DatabaseSpecBuilder specBuilder) {
        var parserFactory = SqlParserFactory.get(parserConfig.dialect());
        var commentHandler = CommentHandlers.createDocCommentHandler();
        var parser = parserFactory.createParser(parserConfig,
                specBuilder,
                commentHandler);
        int failures = 0;
        for (String source : sources) {
            Path fullPath = getDirectory().resolve(source);
            try {
                getLogger().log(Level.INFO, Messages.PARSING_SQL_SOURCE(source));
                String sql = Files.readString(fullPath);
                int errors = parser.parse(sql);
                reportSqlProblems(parser.getProblems());
                if (errors == 0) {
                    getLogger().log(Level.INFO, Messages.PARSED_SQL_SOURCE(source));
                } else {
                    failures++;
                }
            } catch (NoSuchFileException e) {
                getLogger().log(Level.ERROR, Messages.SQL_FILE_NOT_EXIST(fullPath));
                failures++;
            } catch (IOException e) {
                getLogger().log(Level.ERROR, Messages.FAILED_TO_READ_FILE(fullPath));
                failures++;
            }
        }
        if (failures > 0) {
            throw new CommandException(Messages.FOUND_SQL_ERRORS(failures));
        }
    }

    private void reportSqlProblems(List<Problem> problems) {
        var logger = getLogger();
        for (var problem : problems) {
            logger.log(problem.severity(), problem.toString());
        }
    }

    private Map<String, RenderConfig> selectRenderers(RootConfig rootConfig, String[] names) {
        var renderers = rootConfig.renderers();
        if (renderers.isEmpty()) {
            throw new CommandException(Messages.NO_RENDERER_DEFINED());
        }

        if (names == null || names.length == 0) {
            names = renderers.keySet().stream()
                .limit(1).toArray(String[]::new);
        }

        return Stream.of(names)
            .collect(Collectors.toMap(Function.identity(),
                name -> {
                    if (renderers.containsKey(name)) {
                        return renderers.get(name);
                    } else {
                        throw new CommandException(Messages.RENDERER_NOT_DEFINED(name));
                    }
                }
            ));
    }

    private void renderDocument(DatabaseSpec spec, String name, RenderConfig renderConfig, RootConfig rootConfig) {
        var factory = getRendererFactory(renderConfig.format());
        var renderer = factory.createRenderer(renderConfig);
        Path outDir = requireOutputDirectory(rootConfig.outDir());
        try  {
            getLogger().log(Level.INFO, Messages.RENDERING_SPEC(name, outDir));
            renderer.render(spec, outDir);
            getLogger().log(Level.INFO, Messages.RENDERED_SPEC(name, outDir));
        } catch (IOException e) {
            throw new CommandException(Messages.FAILED_TO_WRITE_FILE(outDir), e);
        }
    }

    private RendererFactory getRendererFactory(String format) {
        try {
            return RendererFactory.get(format);
        } catch (Exception e) {
            throw new CommandException(Messages.RENDERER_UNAVAILABLE(format), e);
        }
    }

    private Path requireOutputDirectory(String outDir) {
        var path = getDirectory().resolve(outDir);
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new CommandException(Messages.CANNOT_TO_CREATE_OUTPUT_DIRECTORY(path), e);
        }
    }
}
