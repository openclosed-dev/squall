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

package dev.openclosed.squall.core.parser;

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.parser.CommentHandler;
import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.ParserContext;
import dev.openclosed.squall.api.parser.SqlParser;
import dev.openclosed.squall.api.parser.SqlSyntaxException;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.builder.DatabaseSpecBuilder;
import dev.openclosed.squall.core.base.SnippetExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BaseSqlParser
    implements SqlParser, ParserContext, SqlGrammarEntry, SqlGrammarSupport {

    private final ParserConfig config;
    private final DatabaseSpecBuilder specBuilder;

    private final CommentHandler commentHandler;

    private final List<Problem> problems = new ArrayList<>();
    private int errorCount;

    private SqlTokenizer tokenizer;

    private SnippetExtractor snippetExtractor;

    protected BaseSqlParser(
        ParserConfig config,
        DatabaseSpecBuilder specBuilder,
        CommentHandler commentHandler) {

        this.config = config;
        this.specBuilder = specBuilder;
        this.commentHandler = commentHandler;
    }

    @Override
    public final int parse(CharSequence text) {
        Objects.requireNonNull(text);
        reset(text);
        try {
            statements();
        } catch (SqlSyntaxException e) {
            handleSyntaxError(e);
        } catch (Exception e) {
            throw new RuntimeException("Internal error", e);
        }
        return errorCount;
    }

    @Override
    public final Expression parseExpression(CharSequence text) throws SqlSyntaxException {
        Objects.requireNonNull(text);
        reset(text);
        return expression();
    }

    @Override
    public final List<Problem> getProblems() {
        return List.copyOf(this.problems);
    }

    // ParserContext

    @Override
    public final ParserConfig config() {
        return config;
    }

    @Override
    public final DatabaseSpecBuilder builder() {
        return specBuilder;
    }

    @Override
    public final void reportProblem(System.Logger.Level severity, Message message, Location location) {
        Objects.requireNonNull(severity);
        Objects.requireNonNull(message);
        Objects.requireNonNull(location);

        var source = snippetExtractor.extract(location);
        Problem problem = new SqlProblem(severity, message, location, source);

        this.problems.add(problem);

        if (severity == System.Logger.Level.ERROR) {
            errorCount++;
        }
    }

    // SqlGrammarSupport

    @Override
    public final SqlTokenizer getTokenizer() {
        return tokenizer;
    }

    @Override
    public void withRecovery(Runnable runnable) {
        try {
            runnable.run();
            expect(SpecialSymbol.SEMICOLON);
        } catch (SqlSyntaxException e) {
            handleSyntaxError(e);
            if (next() != Token.EOI) {
                find(SpecialSymbol.SEMICOLON);
            }
        }
    }

    @Override
    public Token next() {
        SqlTokenizer t = getTokenizer();
        Token next = t.next();
        while (next != Token.EOI && next.isComment()) {
            handleComment((CommentToken) next);
            t.consume();
            next = t.next();
        }
        return next;
    }

    @Override
    public void consume() {
        getTokenizer().consume();
    }


    protected void reset(CharSequence text) {
        this.problems.clear();
        this.errorCount = 0;
        this.tokenizer = createTokenizer(text);
        this.snippetExtractor = new SnippetExtractor(text);
    }

    protected abstract SqlTokenizer createTokenizer(CharSequence text);

    private void handleComment(CommentToken token) {
        if (this.commentHandler == null) {
            return;
        }
        var comment = new TextSegment(getTokenizer().text(), token.start(), token.length());
        if (commentHandler.canHandle(comment)) {
            var location = getTokenizer().getTokenLocation();
            commentHandler.handleComment(comment, location, this);
        }
    }

    private void handleSyntaxError(SqlSyntaxException e) {
        reportProblem(System.Logger.Level.ERROR, e.getBundledMessage(), e.getLocation());
    }
}
