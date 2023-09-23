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
interface TabularComponentWriter<T extends Component> {

    TabularComponentWriter<?> EMPTY = new TabularComponentWriter<>() { };

    default void writeHeader() {
    }

    default void writeDataRow(T component) {
    }

    static TabularComponentWriter<Column> columnWriter(
        List<ColumnAttribute> attributes,
        DocBuilder builder,
        WriterContext context,
        Consumer<Column> anchorWriter) {

        if (attributes.isEmpty()) {
            return emptyWriter();
        }

        List<ColumnAttributeWriter> writers = attributes.stream()
            .map(ColumnAttributeWriter::writing).toList();

        return new TabularComponentWriterImpl<>(writers, builder, context, anchorWriter);
    }

    static TabularComponentWriter<Sequence> sequenceWriter(
        List<SequenceAttribute> attributes,
        DocBuilder builder,
        WriterContext context) {

        if (attributes.isEmpty()) {
            return emptyWriter();
        }

        List<SequenceAttributeWriter> writers = attributes.stream()
            .map(SequenceAttributeWriter::writing).toList();

        return new TabularComponentWriterImpl<>(writers, builder, context, t -> { });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> TabularComponentWriter<T> emptyWriter() {
        return (TabularComponentWriter<T>) EMPTY;
    }
}

final class TabularComponentWriterImpl<T extends Component> implements TabularComponentWriter<T> {

    private final List<? extends AttributeWriter<T>> attributeWriters;
    private final DocBuilder builder;
    private final WriterContext context;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    private int nextRowNo;

    TabularComponentWriterImpl(
        List<? extends AttributeWriter<T>> attributeWriters,
        DocBuilder builder,
        WriterContext context,
        Consumer<T> anchorWriter) {
        this.attributeWriters = attributeWriters;
        this.builder = builder;
        this.context = context;
        this.anchorWriter = anchorWriter;
        final var bundle = context.bundle();
        this.titles = attributeWriters.stream()
            .map(p -> bundle.columnHeader(p.name()))
            .toList();
    }

    @Override
    public void writeHeader() {
        this.nextRowNo = 1;
        writeHeaderRow(builder);
        writeDelimiterRow(builder);
    }

    @Override
    public void writeDataRow(T component) {
        writeDataRow(component, this.nextRowNo++, this.builder, this.context);
    }

    private void writeHeaderRow(DocBuilder builder) {
        builder.append("|");
        for (var title : this.titles) {
            builder.appendSpace().append(title).append(" |");
        }
        builder.appendNewLine();
    }

    private void writeDelimiterRow(DocBuilder builder) {
        builder.append("|");
        for (var writer : attributeWriters) {
            builder.appendSpace().append(writer.getSeparator()).append(" |");
        }
        builder.appendNewLine();
    }

    private void writeDataRow(T component, int rowNo, DocBuilder builder, WriterContext context) {
        builder.append("|");
        this.anchorWriter.accept(component);
        for (var writer : this.attributeWriters) {
            builder.appendSpace();
            writer.writeValue(component, rowNo, builder, context);
            builder.append(" |");
        }
        builder.appendNewLine();
    }
}
