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

package dev.openclosed.squall.renderer.asciidoc;

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
interface AsciiDocTableWriter<T extends Component> {

    AsciiDocTableWriter<?> EMPTY = new AsciiDocTableWriter<>() { };

    default void writeHeader() {
    }

    default void writeFooter() {
    }

    default void writeDataRow(T component) {
    }

    static AsciiDocTableWriter<Column> forColumn(
        List<ColumnAttribute> attributes,
        Appender appender,
        RenderContext context,
        Consumer<Column> anchorWriter) {

        if (attributes.isEmpty()) {
            return empty();
        }

        List<ColumnCellWriter> writers = attributes.stream()
            .map(ColumnCellWriter::writing).toList();
        return new AsciiDocTableWriterImpl<>(writers, appender, context, anchorWriter);
    }

    static AsciiDocTableWriter<Sequence> forSequence(
        List<SequenceAttribute> attributes,
        Appender appender,
        RenderContext context) {

        if (attributes.isEmpty()) {
            return empty();
        }

        List<SequenceCellWriter> writers = attributes.stream()
            .map(SequenceCellWriter::writing).toList();

        return new AsciiDocTableWriterImpl<>(writers, appender, context, t -> { });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> AsciiDocTableWriter<T> empty() {
        return (AsciiDocTableWriter<T>) EMPTY;
    }
}

final class AsciiDocTableWriterImpl<T extends Component> implements AsciiDocTableWriter<T> {

    private final List<? extends CellWriter<T>> writers;
    private final Appender appender;
    private final RenderContext context;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    private int nextRowNo;

    AsciiDocTableWriterImpl(
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

        appender.append("[cols=\"");
        int columns = 0;
        for (var writer : this.writers) {
            if (columns++ > 0) {
                appender.append(',');
            }
            appender.append(writer.specifier());
        }
        // options value must be quoted.
        appender.append("\", options=\"header\"]").appendNewLine();
        appender.append("|===").appendNewLine();

        writeHeaderRow(appender);
    }

    @Override
    public void writeFooter() {
        appender.append("|===").appendNewLine();
    }

    @Override
    public void writeDataRow(T component) {
        writeDataRow(component, this.nextRowNo++, this.appender, this.context);
    }

    private void writeHeaderRow(Appender appender) {
        for (var title : this.titles) {
            appender.append("^|").append(title).appendNewLine();
        }
    }

    private void writeDataRow(T component, int rowNo, Appender appender, RenderContext context) {
        appender.appendNewLine();
        int columns = 0;
        for (var writer : this.writers) {
            appender.append('|');
            if (columns++ == 0) {
                this.anchorWriter.accept(component);
            }
            writer.writeValue(component, rowNo, appender, context);
            appender.appendNewLine();
        }
    }
}
