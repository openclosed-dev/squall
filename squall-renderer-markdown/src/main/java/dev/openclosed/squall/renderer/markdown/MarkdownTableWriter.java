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

package dev.openclosed.squall.renderer.markdown;

import dev.openclosed.squall.api.spec.Component;

import java.util.List;
import java.util.ResourceBundle;

final class MarkdownTableWriter<T extends Component, P> {

    private final List<? extends CellProvider<T, P>> providers;
    private final List<String> titles;

    static <T extends Component, P> MarkdownTableWriter<T, P> withProviders(
        List<? extends CellProvider<T, P>> providers, ResourceBundle bundle) {
        return new MarkdownTableWriter<>(providers, bundle);
    }

    private MarkdownTableWriter(List<? extends CellProvider<T, P>> providers, ResourceBundle bundle) {
        this.providers = providers;
        this.titles = providers.stream()
            .map(p -> bundle.getString("column.header." + p.name()))
            .toList();
    }

    void writeHeaderRow(Appender appender) {
        for (var title : this.titles) {
            appender.append("| ").append(title).appendSpace();
        }
        appender.append("|").appendNewLine();
    }

    void writeDelimiterRow(Appender appender) {
        for (var provider : providers) {
            appender.append("| ").append(provider.getSeparator()).appendSpace();
        }
        appender.append("|").appendNewLine();
    }

    void writeDataRow(Appender appender, T component) {
        writeDataRow(appender, component, null, 1);
    }

    void writeDataRow(Appender appender, T component, P parentComponent, int rowNo) {
        for (var provider : this.providers) {
            String value = provider.getValue(component, parentComponent, rowNo);
            appender.append("| ").append(value).appendSpace();
        }
        appender.append("|").appendNewLine();
    }
}
