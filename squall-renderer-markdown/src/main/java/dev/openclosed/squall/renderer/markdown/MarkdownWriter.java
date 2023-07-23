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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ResourceBundle;

import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;

class MarkdownWriter implements SpecVisitor, DelegatingAppender {

    private final RenderConfig config;
    private final Appendable appender;

    private final boolean showDatabase;
    private final boolean showSchema;
    private final MarkdownTableWriter<Column, Table> columnWriter;
    private final MarkdownTableWriter<Sequence, Void> sequenceWriter;

    private int level;

    private final StringBuilder headingNumber = new StringBuilder();

    private static final int MAX_LEVEL = 9;

    private static final String[] HEADING_PREFIX = {
            "", "#", "##", "###", "####", "#####"
    };

    private final int[] baseNumberLength = new int[MAX_LEVEL];

    MarkdownWriter(RenderConfig config, ResourceBundle bundle, Appendable appender) {
        this.config = config;
        this.appender = appender;
        this.showDatabase = !config.hide().contains(Component.Type.DATABASE);
        this.showSchema = !config.hide().contains(Component.Type.SCHEMA);
        this.columnWriter = ColumnCellProvider.tableWriter(bundle, config.columnAttributes());
        this.sequenceWriter = SequenceCellProvider.tableWriter(bundle, config.sequenceAttributes());
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        try {
            spec.walkSpec(this, config.order());
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    // DelegatingAppender

    @Override
    public Appendable getDelegate() {
        return appender;
    }

    // SpecVisitor

    @Override
    public void visit(DatabaseSpec spec) {
        enterLevel();
        writeHeading(spec.title().orElse("Untitled"));
    }

    @Override
    public void leave(DatabaseSpec spec) {
        leaveLevel();
        writeImageDefs();
    }

    @Override
    public void visit(Database database, int ordinal) {
        if (showDatabase) {
            enterLevel();
            writeHeading(database, ordinal);
            append(database.description().orElse("")).append('\n');
        }
    }

    @Override
    public void leave(Database database) {
        if (showDatabase) {
            leaveLevel();
        }
    }

    @Override
    public void visit(Schema schema, int ordinal) {
        if (showSchema) {
            enterLevel();
            writeHeading(schema, ordinal);
            append(schema.description().orElse("")).append('\n');
        }
    }

    @Override
    public void leave(Schema schema) {
        if (showSchema) {
            leaveLevel();
        }
    }

    @Override
    public void visit(Sequence sequence, int ordinal) {
        enterLevel();
        writeHeading(sequence, ordinal);

        sequence.description().ifPresent(
            value -> append(value).appendNewLine());

        appendNewLine();
        sequenceWriter.writeHeaderRow(this);
        sequenceWriter.writeDelimiterRow(this);
        sequenceWriter.writeDataRow(this, sequence);
    }

    @Override
    public void leave(Sequence sequence) {
        appendNewLine();
        leaveLevel();
    }


    @Override
    public void visit(Table table, int ordinal) {
        enterLevel();
        writeHeading(table, ordinal);

        table.description().ifPresent(
            value -> append(value).appendNewLine());

        if (table.hasColumns()) {
            appendNewLine();
            columnWriter.writeHeaderRow(this);
            columnWriter.writeDelimiterRow(this);
        }
    }

    @Override
    public void leave(Table title) {
        appendNewLine();
        leaveLevel();
    }

    @Override
    public void visit(Table table, Column column, int ordinal) {
        this.columnWriter.writeDataRow(this, column, table, ordinal);
    }

    private void enterLevel() {
        if (this.level < MAX_LEVEL) {
            this.level++;
        }
    }

    private void leaveLevel() {
        if (this.level > 0) {
            this.level--;
        }
    }

    private void writeHeading(String title) {
        append(HEADING_PREFIX[this.level])
            .append(' ')
            .append(title)
            .append('\n');
    }

    private void writeHeading(Component component, int ordinal) {

        append(HEADING_PREFIX[this.level]).append(' ');

        if (config.numbering()) {
            headingNumber.setLength(baseNumberLength[level - 1]);
            headingNumber.append(ordinal).append('.');
            append(headingNumber).appendSpace();
            baseNumberLength[level] = headingNumber.length();
        }

        append(component.label().orElse(component.name())).appendSpace();
        append('`').append(component.name()).append('`').appendSpace();

        Badge badge = Badge.mapComponentType(component.type());
        append("![").append(badge.label()).append(']').appendNewLine();
    }

    private void writeImageDefs() {
        appendNewLine();
        for (var badge : Badge.values()) {
            append('[').append(badge.name()).append("]: ");
            append(badge.url()).appendNewLine();
        }
    }
}
