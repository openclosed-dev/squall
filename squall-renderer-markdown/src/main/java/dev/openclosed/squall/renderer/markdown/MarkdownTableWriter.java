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
interface MarkdownTableWriter<T extends Component> {

    default void writeHeaderRow(Appender appender) {
    }

    default void writeDelimiterRow(Appender appender) {
    }

    default void writeDataRow(Appender appender, T component, int rowNo, SpecVisitor.Context context) {
    }

    static MarkdownTableWriter<Column> forColumn(
        List<ColumnAttribute> attributes,
        MessageBundle bundle,
        Consumer<Column> anchorWriter) {
        List<ColumnCellProvider> providers = attributes.stream()
            .map(ColumnCellProvider::provider).toList();
        return MarkdownTableWriterImpl.withProviders(providers, bundle, anchorWriter);
    }

    static MarkdownTableWriter<Sequence> forSequence(
        List<SequenceAttribute> attributes,
        MessageBundle bundle) {
        List<SequenceCellProvider> providers = attributes.stream()
            .map(SequenceCellProvider::provider).toList();
        return MarkdownTableWriterImpl.withProviders(providers, bundle, t -> { });
    }
}
