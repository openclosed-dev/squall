/*
 * Copyright 2023 The Squall Authors
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

package dev.openclosed.squall.api.test.parser;

import dev.openclosed.squall.api.parser.CommentProcessor;
import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.util.Records;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class DocCommentProcessorTest {

    public static Stream<SqlTestCase> testComments() {
        return SqlTestCase.loadFrom("doc-comment.md",
                DocCommentProcessorTest.class).stream();
    }

    @ParameterizedTest
    @MethodSource
    public void testComments(SqlTestCase test) {
        var context = new TestParserContext();
        var processor = CommentProcessor.newDocCommentProcessor();
        var sql = test.firstSql();
        int start = sql.indexOf("/**");
        int end = sql.lastIndexOf("*/");
        var comment = sql.subSequence(start, end + 2);
        processor.processComment(comment, new Location(1, start + 1, start), context);
        var annotations = context.getAnnotations();
        var problems = context.getProblems();
        var actual = annotations.stream().map(Records::toMap).toList();
        var expected = test.jsonAsMaps();

        for (var problem : problems) {
            System.out.println(problem.message().get());
        }

        assertThat(actual).isEqualTo(expected);
        assertThat(context.getProblems()).isEmpty();
    }
}
