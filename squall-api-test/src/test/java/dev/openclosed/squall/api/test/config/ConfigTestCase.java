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

package dev.openclosed.squall.api.test.config;

import java.util.List;

/**
 * A test case for loading configuration.
 * @param title the title of this test case.
 * @param json the JSON-format text to parse.
 * @param output expected output.
 */
record ConfigTestCase(String title, String json, String output) {

    @Override
    public String toString() {
        return title();
    }

    public static List<ConfigTestCase> loadFrom(String name) {
        var in = ConfigTestCase.class.getResourceAsStream(name);
        return TestCaseParser.withBuilder(new ConfigTestCaseBuilder()).parse(in);
    }

    private static class ConfigTestCaseBuilder implements TestCaseBuilder<ConfigTestCase> {

        private String title = "";
        private String json = "";
        private String output = "";

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public void addCode(String code, String language) {
            switch (language) {
            case "json" -> {
                this.json = code;
            }
            default -> {
                this.output = code;
            }
            }
        }

        @Override
        public ConfigTestCase build() {
            var built = new ConfigTestCase(title, json, output);
            json = "";
            output = "";
            return built;
        }
    }
}
