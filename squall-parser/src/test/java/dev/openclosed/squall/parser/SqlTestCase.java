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

package dev.openclosed.squall.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.openclosed.squall.api.text.json.JsonReader;

public record SqlTestCase(String title, List<String> sql, Map<String, String> expected, String output) {

    private static final JsonReader JSON_READER = JsonReader.newReader();

    public String firstSql() {
        return sql.get(0);
    }

    @Override
    public String toString() {
        return title;
    }

    public String json() {
        return expected.getOrDefault("json", "");
    }

    public String text() {
        return expected.getOrDefault("text", "");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> jsonAsMap() {
        return (Map<String, Object>) JSON_READER.readObject(json());
    }

    @SuppressWarnings("unchecked")
    public List<String> jsonAsStringList() {
        return (List<String>) JSON_READER.readArray(json());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> jsonAsMaps() {
        return (List<Map<String, Object>>) JSON_READER.readArray(json());
    }

    public static List<SqlTestCase> loadFrom(InputStream in) {
        Objects.requireNonNull(in);
        return TestCaseParser.withBuilder(new SqlTestCaseBuilder()).parse(in);
    }

    public static List<SqlTestCase> loadFrom(String name, Class<?> clazz) {
        return loadFrom(clazz.getResourceAsStream(name));
    }

    private static class SqlTestCaseBuilder implements TestCaseBuilder<SqlTestCase> {

        private String title = "";
        private List<String> sql = new ArrayList<String>();
        private Map<String, String> expected = new HashMap<>();
        private String output = "";

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public void addCode(String code, String language) {
            switch (language) {
                case "sql" -> this.sql.add(code);
                case "" -> {
                    this.output = code;
                }
                default -> expected.put(language, code);
            }
        }

        @Override
        public SqlTestCase build() {
            var built = new SqlTestCase(title, List.copyOf(sql), Map.copyOf(expected), output);
            sql.clear();
            output = "";
            expected.clear();
            return built;
        }
    }
}
