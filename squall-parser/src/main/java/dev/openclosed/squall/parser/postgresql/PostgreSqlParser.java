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

package dev.openclosed.squall.parser.postgresql;

import dev.openclosed.squall.api.parser.CommentProcessor;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.spec.ExpressionFactory;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import dev.openclosed.squall.parser.basic.BaseSqlParser;
import dev.openclosed.squall.parser.basic.BuildingSqlHandler;
import dev.openclosed.squall.parser.basic.Keyword;
import dev.openclosed.squall.parser.basic.SqlTokenizer;

import java.util.Map;

final class PostgreSqlParser extends BaseSqlParser
    implements PostgreSqlGrammar, BuildingSqlHandler {

    private final DatabaseSpecBuilder builder;
    private final ExpressionFactory expressionFactory;
    private final Map<String, Keyword> keywords;

    PostgreSqlParser(
        ParserConfig config,
        DatabaseSpecBuilder builder,
        ExpressionFactory expressionFactory,
        CommentProcessor commentProcessor,
        Map<String, Keyword> keywords) {
        super(config, commentProcessor);
        this.builder = builder;
        this.expressionFactory = expressionFactory;
        this.keywords = keywords;
    }

    // BaseSqlParser

    @Override
    protected SqlTokenizer createTokenizer(CharSequence text) {
        return new PostgreSqlTokenizer(text, this.keywords);
    }

    // BuildingSqlHandler

    @Override
    public DatabaseSpecBuilder builder() {
        return this.builder;
    }

    // SqlGrammarSupport

    @Override
    public ExpressionFactory expressionFactory() {
        return this.expressionFactory;
    }
}

