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

package dev.openclosed.squall.api.config;

import static org.assertj.core.api.Assertions.assertThat;

import dev.openclosed.squall.api.base.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class ObjectFactoryTest {

    private ObjectFactory sut;

    @BeforeEach
    public void setUp() {
        MessageBundle bundle = MessageBundle.forLocale(Locale.ENGLISH);
        this.sut = new ObjectFactory(bundle);
    }

    record Stuff(
        short a,
        int b,
        long c,
        String d,
        boolean e,
        List<String> f,
        Set<String> g,
        Map<String, String> h,
        Optional<String> i
    ) {
        Stuff() {
            this(
                (short) 0,
                0,
                0,
                "",
                false,
                Collections.emptyList(),
                Collections.emptySet(),
                Collections.emptyMap(),
                Optional.empty()
            );
        }
    }

    @Test
    void shouldCreateRecordFromMap() {

        var map = Map.of(
            "a", 32767,
            "b", 2147483647,
            "c", 9223372036854775807L,
            "d", "Hello World",
            "e", true,
            "f", List.of("one", "two", "three"),
            "g", List.of("foo", "bar", "baz"),
            "h", Map.of(
                "key1", "value1",
                "key2", "value2"
            ),
            "i", "may be omitted"
        );

        Stuff expected = new Stuff(
            (short) 32767,
            2147483647,
            9223372036854775807L,
            "Hello World",
            true,
            List.of("one", "two", "three"),
            Set.of("foo", "bar", "baz"),
            Map.of(
                "key1", "value1",
                "key2", "value2"
            ),
            Optional.of("may be omitted")
        );

        Stuff actual = sut.createRecord(map, Stuff.class, ObjectFactoryTest::reportProblem);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateRecordFromEmptyMap() {
        Stuff expected = new Stuff();
        Map<String, Object> map = Collections.emptyMap();
        Stuff actual = sut.createRecord(map, Stuff.class, ObjectFactoryTest::reportProblem);
        assertThat(actual).isEqualTo(expected);
    }

    private static void reportProblem(Problem problem) {
        System.out.println(problem.toString());
    }
}
