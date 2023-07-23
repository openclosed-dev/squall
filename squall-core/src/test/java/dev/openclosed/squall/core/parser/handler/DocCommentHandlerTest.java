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

package dev.openclosed.squall.core.parser.handler;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.parser.CommentHandlers;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.core.parser.SqlTestCase;

public final class DocCommentHandlerTest {

    public static Stream<SqlTestCase> testComments() {
        return SqlTestCase.loadFrom("doc-comments.md",
                DocCommentHandlerTest.class).stream();
    }

    @ParameterizedTest
    @MethodSource
    public void testComments(SqlTestCase test) {
        var context = new TestParserContext();
        var handler = CommentHandlers.createDocCommentHandler();
        var sql = test.firstSql();
        int start = sql.indexOf("/**");
        int end = sql.lastIndexOf("*/");
        var comment = sql.subSequence(start, end + 2);
        handler.handleComment(comment, new Location(1, start + 1, start), context);
        var annotations = context.builder().useAnnotations();
        var actual = annotations.stream().map(DocAnnotation::toMap).toList();
        var expected = test.jsonAsMaps();
        assertThat(actual).isEqualTo(expected);
    }
}
