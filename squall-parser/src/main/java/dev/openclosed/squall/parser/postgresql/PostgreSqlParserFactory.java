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
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Dialect;
import dev.openclosed.squall.api.spec.ExpressionFactory;
import dev.openclosed.squall.api.spec.MajorDialect;
import dev.openclosed.squall.parser.basic.Keyword;

import java.util.Map;
import java.util.Objects;

public final class PostgreSqlParserFactory implements SqlParserFactory {

    private ExpressionFactory expressionFactory;
    private Map<String, Keyword> keywords;

    @Override
    public Dialect dialect() {
        return MajorDialect.POSTGRESQL;
    }

    @Override
    public SqlParser createParser(
        ParserConfig config,
        DatabaseSpec.Builder builder,
        CommentProcessor commentProcessor) {

        Objects.requireNonNull(config);
        Objects.requireNonNull(builder);
        Objects.requireNonNull(commentProcessor);

        return new PostgreSqlParser(
            config, builder, getExpressionFactory(), commentProcessor, getKeywords());
    }

    private ExpressionFactory getExpressionFactory() {
        if (this.expressionFactory == null) {
            this.expressionFactory = ExpressionFactory.newFactory();
        }
        return this.expressionFactory;
    }

    private Map<String, Keyword> getKeywords() {
        if (this.keywords == null) {
            this.keywords = PostgreSqlKeyword.valuesAsMap();
        }
        return this.keywords;
    }
}
