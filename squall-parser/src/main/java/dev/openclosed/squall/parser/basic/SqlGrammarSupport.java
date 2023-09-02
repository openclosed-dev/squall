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

package dev.openclosed.squall.parser.basic;

import dev.openclosed.squall.api.parser.ParserConfig;
import dev.openclosed.squall.api.parser.SqlSyntaxException;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.ExpressionFactory;
import dev.openclosed.squall.parser.Messages;

import java.util.List;

public interface SqlGrammarSupport {

    ParserConfig config();

    SqlTokenizer getTokenizer();

    DatabaseSpec.Builder builder();

    ExpressionFactory expressionFactory();

    void withRecovery(Runnable runnable);

    /**
     * Returns the next token.
     * @return the next token if exists, {@link Token#EOI} if not exists.
     */
    Token next();

    void consume();

    List<DocAnnotation> captureAnnotations();

    default void find(Token token) {
        Token next;
        while ((next = next()) != token) {
            if (next == Token.EOI) {
                unexpectedEndError();
                return;
            }
            consume();
        }
    }

    default void expect(Token token) {
        Token next = next();
        if (next != token) {
            syntaxError(next);
        }
    }

    default void expect(Keyword keyword) {
        Token next = next();
        if (!next.isSameAs(keyword)) {
            syntaxError(next);
        }
    }

    default Token expectType(TokenType type) {
        Token next = next();
        if (next.type() == type) {
            return next;
        } else {
            syntaxError(next);
            // never reach here
            return null;
        }
    }

    default Keyword expectKeyword() {
        Token next = next();
        if (next instanceof Keyword keyword) {
            return keyword;
        } else {
            syntaxError(next);
            // never reach here
            return null;
        }
    }

    default String expectIdentifier(IdentifierType type) {
        Token next = next();
        if (!next.isIdentifier(type)) {
            syntaxError(next);
            return null;
        }
        return next.toIdentifier();
    }

    default void syntaxError(Token token) {
        if (token == Token.EOI) {
            unexpectedEndError();
        } else {
            var t = getTokenizer();
            var location = t.getTokenLocation();
            var text = t.getTokenText();
            throw new SqlSyntaxException(Messages.SYNTAX_ERROR(text), location);
        }
    }

    default void unexpectedEndError() {
        var location = getTokenizer().getLocation();
        throw new SqlSyntaxException(Messages.UNEXPECTED_END_OF_INPUT(), location);
    }
}
