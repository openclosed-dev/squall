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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ResourceBundle;

import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.DocAnnotationType;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.Sequence;
import dev.openclosed.squall.api.spec.SpecVisitor;
import dev.openclosed.squall.api.spec.Table;

/**
 * Database specification writer.
 */
class SpecWriter implements SpecVisitor, DelegatingAppender {

    private final RenderConfig config;
    private final ResourceBundle bundle;
    private final Appendable appender;

    private final boolean hideDatabase;
    private final boolean hideSchema;
    private final MarkdownTableWriter<Column, Table> columnWriter;
    private final MarkdownTableWriter<Sequence, Void> sequenceWriter;

    private int databaseCount;

    private final StringBuilder headingNumber = new StringBuilder();

    private static final String[] HEADING_PREFIX = {
        "#", "##", "###", "####", "#####", "######", "#######"
    };

    private final Deque<Heading> headings = new ArrayDeque<>();

    SpecWriter(RenderConfig config, ResourceBundle bundle, Appendable appender) {
        this.config = config;
        this.bundle = bundle;
        this.appender = appender;
        this.columnWriter = ColumnCellProvider.tableWriter(bundle, config.columnAttributes());
        this.sequenceWriter = SequenceCellProvider.tableWriter(bundle, config.sequenceAttributes());
        // visibility of components
        this.hideDatabase = config.hide().contains(Component.Type.DATABASE);
        this.hideSchema = config.hide().contains(Component.Type.SCHEMA);
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.databaseCount = spec.databases().size();
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
        String title = spec.title().orElse("Untitled");
        append("# ").append(title).appendNewLine();

        enterLevel();
    }

    @Override
    public void leave(DatabaseSpec spec) {
        leaveLevel();
        writeImageDefs();
    }

    @Override
    public void visit(Database database, int ordinal) {
        if (hideDatabase) {
            return;
        }

        if (databaseCount > 1) {
            writeHeading(database);
            enterLevel();
        }

        writeDescriptionSection(database);
    }

    @Override
    public void leave(Database database) {
        if (hideDatabase || databaseCount <= 1) {
            return;
        }
        leaveLevel();
    }

    @Override
    public void visit(Schema schema, int ordinal) {
        if (hideSchema) {
            return;
        }

        writeHeading(schema);
        enterLevel();

        writeDescriptionSection(schema);
    }

    @Override
    public void leave(Schema schema) {
        if (hideSchema) {
            return;
        }
        leaveLevel();
    }

    @Override
    public void visit(Sequence sequence, int ordinal, Schema schema) {
        writeHeading(sequence);
        writeDescriptionSection(sequence);

        appendNewLine();
        sequenceWriter.writeHeaderRow(this);
        sequenceWriter.writeDelimiterRow(this);
        sequenceWriter.writeDataRow(this, sequence);
    }

    @Override
    public void leave(Sequence sequence) {
    }

    @Override
    public void visit(Table table, int ordinal, Schema schema) {
        writeHeading(table);
        writeDescriptionSection(table);

        if (table.hasColumns()) {
            appendNewLine();
            columnWriter.writeHeaderRow(this);
            columnWriter.writeDelimiterRow(this);
        }
    }

    @Override
    public void leave(Table title) {
    }

    @Override
    public void visit(Column column, int ordinal, Table table) {
        this.columnWriter.writeDataRow(this, column, table, ordinal);
    }

    private void enterLevel() {
        this.headings.addLast(new Heading(headingNumber.length()));
    }

    private void leaveLevel() {
        this.headings.removeLast();
    }

    private void writeHeading(Component component) {

        final int level = this.headings.size();
        appendNewLine();
        append(HEADING_PREFIX[level]);

        if (config.numbering()) {
            writeHeadingNumber();
        }

        writeHeadingText(component);

        Badge badge = Badge.mapComponentType(component.type());
        appendSpace();
        append("![").append(badge.label()).append(']').appendNewLine();
    }

    private void writeHeadingNumber() {
        appendSpace();
        Heading heading = headings.getLast();
        headingNumber.setLength(heading.baseLength());
        headingNumber.append(heading.nextOrdinal()).append('.');
        append(headingNumber);
    }

    private void writeHeadingText(Component component) {
        final String name = component.qualifiedName();
        final boolean deprecated = component.isDeprecated();
        component.label().ifPresentOrElse(label -> {
            writeName(label, deprecated);
            writeNameAsCode(name, deprecated);
        }, () -> {
            writeName(name, deprecated);
        });
    }

    private void writeName(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        appendSpace();
        if (deprecated) {
            append("~~").append(name).append("~~");
        } else {
            append(name);
        }
    }

    private void writeNameAsCode(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        appendSpace();
        if (deprecated) {
            append("~~`").append(name).append("`~~");
        } else {
            append('`').append(name).append('`');
        }
    }

    private void writeDescriptionSection(Component component) {
        if (component.isDeprecated()) {
            writeDeprecationNotice(component);
        }
        component.description().ifPresent(description ->
            appendNewLine().append(description).appendNewLine()
            );
    }

    private void writeDeprecationNotice(Component component) {
        appendNewLine();
        append("**").append(getMessage("deprecated")).append("**");
        String text = component.getFirstAnnotation(DocAnnotationType.DEPRECATED).get().value();
        if (!text.isEmpty()) {
            appendSpace().append(text);
        }
        appendNewLine();
    }

    private void writeImageDefs() {
        appendNewLine();
        for (var badge : Badge.values()) {
            append('[').append(badge.name()).append("]: ");
            append(badge.url()).appendNewLine();
        }
    }

    private String getMessage(String key) {
        return bundle.getString(key);
    }

    static class Heading {
        private final int baseLength;
        private int ordinal;

        Heading(int baseLength) {
            this.baseLength = baseLength;
        }

        int baseLength() {
            return baseLength;
        }

        int nextOrdinal() {
            return ++ordinal;
        }
    }
}
