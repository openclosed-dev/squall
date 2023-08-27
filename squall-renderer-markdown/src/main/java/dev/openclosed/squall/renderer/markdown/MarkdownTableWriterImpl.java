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

import dev.openclosed.squall.api.renderer.support.Appender;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.SpecVisitor;

import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

final class MarkdownTableWriterImpl<T extends Component> implements MarkdownTableWriter<T> {

    private static final MarkdownTableWriter<?> EMPTY = new MarkdownTableWriter<>() { };

    private final List<? extends CellProvider<T>> providers;
    private final ResourceBundle bundle;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    /**
     * Create a new table writer with cell value providers.
     * @param providers the providers of cell value in the table.
     * @param bundle the message bundle for localization.
     * @param anchorWriter the callback for writing anchor to each table row.
     * @return the newly created table writer.
     * @param <T> the type of the component written as the table rows.
     */
    static <T extends Component> MarkdownTableWriter<T> withProviders(
        List<? extends CellProvider<T>> providers,
        ResourceBundle bundle,
        Consumer<T> anchorWriter) {
        if (providers.isEmpty()) {
            @SuppressWarnings("unchecked")
            MarkdownTableWriter<T> empty = (MarkdownTableWriter<T>) EMPTY;
            return empty;
        } else {
            return new MarkdownTableWriterImpl<>(providers, bundle, anchorWriter);
        }
    }

    private MarkdownTableWriterImpl(
        List<? extends CellProvider<T>> providers,
        ResourceBundle bundle,
        Consumer<T> anchorWriter) {
        this.providers = providers;
        this.bundle = bundle;
        this.anchorWriter = anchorWriter;
        this.titles = providers.stream()
            .map(p -> bundle.getString("column.header." + p.name()))
            .toList();
    }

    @Override
    public void writeHeaderRow(Appender appender) {
        appender.append("|");
        for (var title : this.titles) {
            appender.appendSpace().append(title).append(" |");
        }
        appender.appendNewLine();
    }

    @Override
    public void writeDelimiterRow(Appender appender) {
        appender.append("|");
        for (var provider : providers) {
            appender.appendSpace().append(provider.getSeparator()).append(" |");
        }
        appender.appendNewLine();
    }

    @Override
    public void writeDataRow(Appender appender, T component, int rowNo, SpecVisitor.Context context) {
        appender.append("|");
        this.anchorWriter.accept(component);
        for (var provider : this.providers) {
            String value = provider.getLocalizedValue(component, rowNo, context, this.bundle);
            appender.appendSpace().append(value).append(" |");
        }
        appender.appendNewLine();
    }
}
