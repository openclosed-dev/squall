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
import dev.openclosed.squall.api.renderer.MessageBundle;
import dev.openclosed.squall.api.renderer.SequenceAttribute;
import dev.openclosed.squall.api.renderer.support.Appender;
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;

import java.util.List;
import java.util.function.Consumer;

/**
 * A table writer in Markdown format.
 * @param <T> the type of the component written as the table rows.
 */
interface AsciiDocTableWriter<T extends Component> {

    AsciiDocTableWriter<?> EMPTY = new AsciiDocTableWriter<>() { };

    default void writeHeader(Appender appender) {
    }

    default void writeFooter(Appender appender) {
    }

    default void writeDataRow(Appender appender, T component, SpecVisitor.Context context) {
    }

    static AsciiDocTableWriter<Column> forColumn(
        List<ColumnAttribute> attributes,
        MessageBundle bundle,
        Consumer<Column> anchorWriter) {
        List<ColumnCellProvider> providers = attributes.stream()
            .map(ColumnCellProvider::provider).toList();
        if (providers.isEmpty()) {
            return empty();
        }
        return new AsciiDocTableWriterImpl<>(providers, bundle, anchorWriter);
    }

    static AsciiDocTableWriter<Sequence> forSequence(
        List<SequenceAttribute> attributes,
        MessageBundle bundle) {
        List<SequenceCellProvider> providers = attributes.stream()
            .map(SequenceCellProvider::provider).toList();
        if (providers.isEmpty()) {
            return empty();
        }
        return new AsciiDocTableWriterImpl<>(providers, bundle, t -> { });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Component> AsciiDocTableWriter<T> empty() {
        return (AsciiDocTableWriter<T>) EMPTY;
    }
}

final class AsciiDocTableWriterImpl<T extends Component> implements AsciiDocTableWriter<T> {

    private final List<? extends CellProvider<T>> providers;
    private final MessageBundle bundle;
    private final Consumer<T> anchorWriter;
    private final List<String> titles;

    private int nextRowNo;

    AsciiDocTableWriterImpl(
        List<? extends CellProvider<T>> providers,
        MessageBundle bundle,
        Consumer<T> anchorWriter) {
        this.providers = providers;
        this.bundle = bundle;
        this.anchorWriter = anchorWriter;
        this.titles = providers.stream()
            .map(p -> bundle.columnHeader(p.name()))
            .toList();
    }

    @Override
    public void writeHeader(Appender appender) {
        this.nextRowNo = 1;

        appender.append("[cols=\"");
        int columns = 0;
        for (var provider : this.providers) {
            if (columns++ > 0) {
                appender.append(',');
            }
            appender.append(provider.specifier());
        }
        // options value must be quoted.
        appender.append("\", options=\"header\"]").appendNewLine();
        appender.append("|===").appendNewLine();

        // Header row
        for (var title : this.titles) {
            appender.append("^|").append(title).appendNewLine();
        }
    }

    @Override
    public void writeFooter(Appender appender) {
        appender.append("|===").appendNewLine();
    }

    @Override
    public void writeDataRow(Appender appender, T component, SpecVisitor.Context context) {
        final int rowNo = this.nextRowNo++;
        appender.appendNewLine();
        int columns = 0;
        for (var provider : this.providers) {
            appender.append('|');
            if (columns++ == 0) {
                this.anchorWriter.accept(component);
            }
            var value = provider.getLocalizedValue(component, rowNo, context, this.bundle);
            appender.append(value).appendNewLine();
        }
    }
}
