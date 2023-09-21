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

import dev.openclosed.squall.api.renderer.ColumnAttribute;
import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.renderer.support.Appender;
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Sequence;

import java.util.List;
import java.util.function.Consumer;

/**
 * A table writer in Markdown format.
 * @param <T> the type of the component written as the table rows.
 */
interface MarkdownTableWriter<T extends Component> {

    MarkdownTableWriter<?> EMPTY = new MarkdownTableWriter<>() { };

    default void writeHeaderRow(Appender appender) {
    }

    default void writeDelimiterRow(Appender appender) {
    }

    default void writeDataRow(Appender appender, T component) {
    }

    static MarkdownTableWriter<Column> forColumn(
        List<ColumnAttribute> attributes,
        RenderContext context,
        Consumer<Column> anchorWriter) {
        List<ColumnCellProvider> providers = attributes.stream()
            .map(ColumnCellProvider::provider).toList();
        if (providers.isEmpty()) {
            return empty();
        }
        return new MarkdownTableWriterImpl<>(providers, context, anchorWriter);
    }

    static MarkdownTableWriter<Sequence> forSequence(
        List<SequenceAttribute> attributes,
        RenderContext context) {
        List<SequenceCellProvider> providers = attributes.stream()
            .map(SequenceCellProvider::provider).toList();
        if (providers.isEmpty()) {
            return empty();
        }
        return new MarkdownTableWriterImpl<>(providers, context, t -> { });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> MarkdownTableWriter<T> empty() {
        return (MarkdownTableWriter<T>) EMPTY;
    }
}

final class MarkdownTableWriterImpl<T extends Component> implements MarkdownTableWriter<T> {

    private final List<? extends CellProvider<T>> providers;
    private final RenderContext context;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    private int nextRowNo;

    MarkdownTableWriterImpl(
        List<? extends CellProvider<T>> providers,
        RenderContext context,
        Consumer<T> anchorWriter) {
        this.providers = providers;
        this.context = context;
        this.anchorWriter = anchorWriter;
        final var bundle = context.bundle();
        this.titles = providers.stream()
            .map(p -> bundle.columnHeader(p.name()))
            .toList();
    }

    @Override
    public void writeHeaderRow(Appender appender) {
        this.nextRowNo = 1;
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
    public void writeDataRow(Appender appender, T component) {
        final int rowNo = this.nextRowNo++;
        appender.append("|");
        this.anchorWriter.accept(component);
        for (var provider : this.providers) {
            String value = provider.getValue(component, rowNo, this.context);
            appender.appendSpace().append(value).append(" |");
        }
        appender.appendNewLine();
    }
}
