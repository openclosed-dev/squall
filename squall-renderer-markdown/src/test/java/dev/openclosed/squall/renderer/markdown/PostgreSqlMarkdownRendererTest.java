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

package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.parser.CommentHandlers;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.MajorDialect;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

class PostgreSqlMarkdownRendererTest extends MarkdownRendererTest {

    private static final Dialect DIALECT = MajorDialect.POSTGRESQL;
    private static SqlParserFactory parserFactory;

    @BeforeAll
    public static void setUpOnce() throws IOException {
        setUpBase(DIALECT);
        parserFactory = SqlParserFactory.get(DIALECT);
    }

    public static Stream<RenderTest> tests() {
        return Stream.of(
            "database",
            "schema",
            "table",
            "hide-database-and-schema",
            "qualified-sequence",
            "qualified-table"
        ).flatMap(MarkdownRendererTest::loadTest);
    }

    @ParameterizedTest
    @MethodSource("tests")
    public void testRenderer(RenderTest test) throws IOException {
        testRenderer(test, DIALECT);
    }

    @Override
    protected SqlParser createParser(DatabaseSpecBuilder builder) {
        return parserFactory.createParser(
            ParserConfig.getDefault(),
            builder,
            CommentHandlers.createDocCommentHandler());
    }
}
