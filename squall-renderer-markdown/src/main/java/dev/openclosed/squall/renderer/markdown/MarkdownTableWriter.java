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

    default void writeHeader() {
    }

    default void writeDataRow(T component) {
    }

    static MarkdownTableWriter<Column> forColumn(
        List<ColumnAttribute> attributes,
        Appender appender,
        RenderContext context,
        Consumer<Column> anchorWriter) {

        if (attributes.isEmpty()) {
            return empty();
        }

        List<ColumnCellWriter> writers = attributes.stream()
            .map(ColumnCellWriter::writing).toList();

        return new MarkdownTableWriterImpl<>(writers, appender, context, anchorWriter);
    }

    static MarkdownTableWriter<Sequence> forSequence(
        List<SequenceAttribute> attributes,
        Appender appender,
        RenderContext context) {

        if (attributes.isEmpty()) {
            return empty();
        }

        List<SequenceCellWriter> writers = attributes.stream()
            .map(SequenceCellWriter::writing).toList();

        return new MarkdownTableWriterImpl<>(writers, appender, context, t -> { });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> MarkdownTableWriter<T> empty() {
        return (MarkdownTableWriter<T>) EMPTY;
    }
}

final class MarkdownTableWriterImpl<T extends Component> implements MarkdownTableWriter<T> {

    private final List<? extends CellWriter<T>> writers;
    private final Appender appender;
    private final RenderContext context;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    private int nextRowNo;

    MarkdownTableWriterImpl(
        List<? extends CellWriter<T>> writers,
        Appender appender,
        RenderContext context,
        Consumer<T> anchorWriter) {
        this.writers = writers;
        this.appender = appender;
        this.context = context;
        this.anchorWriter = anchorWriter;
        final var bundle = context.bundle();
        this.titles = writers.stream()
            .map(p -> bundle.columnHeader(p.name()))
            .toList();
    }

    @Override
    public void writeHeader() {
        this.nextRowNo = 1;
        writeHeaderRow(appender);
        writeDelimiterRow(appender);
    }

    @Override
    public void writeDataRow(T component) {
        writeDataRow(component, this.nextRowNo++, this.appender, this.context);
    }

    private void writeHeaderRow(Appender appender) {
        appender.append("|");
        for (var title : this.titles) {
            appender.appendSpace().append(title).append(" |");
        }
        appender.appendNewLine();
    }

    private void writeDelimiterRow(Appender appender) {
        appender.append("|");
        for (var writer : writers) {
            appender.appendSpace().append(writer.getSeparator()).append(" |");
        }
        appender.appendNewLine();
    }

    private void writeDataRow(T component, int rowNo, Appender appender, RenderContext context) {
        appender.append("|");
        this.anchorWriter.accept(component);
        for (var writer : this.writers) {
            appender.appendSpace();
            writer.writeValue(component, rowNo, appender, context);
            appender.append(" |");
        }
        appender.appendNewLine();
    }
}
