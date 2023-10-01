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
import dev.openclosed.squall.api.parser.MessageBundle;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.expression.ExpressionFactory;
import dev.openclosed.squall.parser.basic.BaseSqlParser;
import dev.openclosed.squall.parser.basic.Keyword;
import dev.openclosed.squall.parser.basic.NameResolver;
import dev.openclosed.squall.parser.basic.SqlTokenizer;

import java.util.Map;

final class PostgreSqlParser extends BaseSqlParser implements PostgreSqlGrammar {

    private final DatabaseSpec.Builder builder;
    private final ExpressionFactory expressionFactory;
    private final NameResolver resolver;
    private final Map<String, Keyword> keywords;

    PostgreSqlParser(
        ParserConfig config,
        DatabaseSpec.Builder builder,
        CommentProcessor commentProcessor,
        MessageBundle messageBundle,
        Map<String, Keyword> keywords) {
        super(config, commentProcessor, messageBundle);
        this.builder = builder;
        this.expressionFactory = builder.getExpressionFactory();
        this.resolver = new NameResolver(config.defaultSchema());
        this.keywords = keywords;
    }

    // BaseSqlParser

    @Override
    protected SqlTokenizer createTokenizer(CharSequence text, MessageBundle messageBundle) {
        return new PostgreSqlTokenizer(text, messageBundle, this.keywords);
    }

    // SqlGrammarSupport

    @Override
    public DatabaseSpec.Builder builder() {
        return this.builder;
    }

    @Override
    public ExpressionFactory expressionFactory() {
        return this.expressionFactory;
    }

    @Override
    public NameResolver resolver() {
        return this.resolver;
    }
}

