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

import dev.openclosed.squall.api.renderer.MessageBundle;
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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Set;

/**
 * A writer of specification document.
 */
final class SpecDocumentWriter implements SpecVisitor, WriterContext {

    private final RenderConfig config;
    private final MessageBundle bundle;
    private final DocBuilder builder;

    private final Set<Component.Type> show;

    private final TabularComponentWriter<Column> columnWriter;
    private final TabularComponentWriter<Sequence> sequenceWriter;

    private int level;

    private Table currentTable;

    SpecDocumentWriter(RenderConfig config, MessageBundle bundle, Appendable appendable) {
        this.config = config;
        this.bundle = bundle;
        this.builder = new DocBuilder(appendable);

        this.show = config.show();

        this.columnWriter = TabularComponentWriter.columnWriter(
            config.columnAttributes(), this.builder, this, this::writeColumnAnchor);
        this.sequenceWriter = TabularComponentWriter.sequenceWriter(
            config.sequenceAttributes(), this.builder, this);
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.level = 0;
        this.currentTable = null;
        try {
            startSpec(spec);
            spec.walkSpec(config.order(), this);
            finishSpec();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    // SpecVisitor

    @Override
    public void visit(Database database) {
        if (!shouldRender(Component.Type.DATABASE)) {
            visitChildren(database);
            return;
        }

        writeDescription(database);

        visitChildren(database);
    }

    @Override
    public void visit(Schema schema) {
        if (!shouldRender(Component.Type.SCHEMA)) {
            visitChildren(schema);
            return;
        }

        writeHeading(schema);
        enterLevel();

        writeDescription(schema);

        visitChildren(schema);

        leaveLevel();
    }

    @Override
    public void visit(Sequence sequence) {
        if (!shouldRender(Component.Type.SEQUENCE)) {
            return;
        }

        writeHeading(sequence);
        writeDescription(sequence);

        builder.appendNewLine();
        this.sequenceWriter.writeHeader();
        this.sequenceWriter.writeDataRow(sequence);
        this.sequenceWriter.writeFooter();
    }

    @Override
    public void visit(Table table) {
        if (!shouldRender(Component.Type.TABLE)) {
            return;
        }

        this.currentTable = table;
        writeHeading(table);
        writeDescription(table);

        if (shouldRender(Component.Type.COLUMN) && table.hasColumns()) {
            builder.appendNewLine();
            this.columnWriter.writeHeader();

            visitChildren(table);
        }

        this.columnWriter.writeFooter();
        this.currentTable = null;
    }

    @Override
    public void visit(Column column) {
        if (shouldRender(Component.Type.COLUMN)) {
            this.columnWriter.writeDataRow(column);
        }
    }

    @Override
    public void visitChildren(Component component) {
        visitChildren(component, config.order());
    }

    // RenderContext

    @Override
    public MessageBundle bundle() {
        return this.bundle;
    }

    @Override
    public Table currentTable() {
        return this.currentTable;
    }

    //

    private void startSpec(DatabaseSpec spec) {
        var metadata = spec.getMetadataOrDefault();
        builder.append("= ").append(metadata.title()).appendNewLine();

        writeMetadata(":author:", metadata.author());
        writeMetadata(":revnumber:", metadata.version());
        writeMetadata(":revdate:", metadata.date());

        builder
            .append(":icons: font").appendNewLine()
            .append(":icon-set: fas").appendNewLine();

        enterLevel();
    }

    private void finishSpec() {
        leaveLevel();
    }

    private boolean shouldRender(Component.Type type) {
        return this.show.contains(type);
    }

    private void enterLevel() {
        this.level++;
    }

    private void leaveLevel() {
        assert this.level > 0;
        this.level--;
    }

    private void writeHeading(Component component) {
        builder.appendNewLine();
        writeSectionAnchor(component);
        builder.appendSectionMarker(level);

        writeHeadingText(component);
        builder.appendNewLine();
    }

    private void writeSectionAnchor(Component component) {
        builder.append("[id=").append(component.fullName()).append(']').appendNewLine();
    }

    private void writeHeadingText(Component component) {
        final String name = component.qualifiedName();
        final boolean deprecated = component.isDeprecated();
        component.label().ifPresentOrElse(label -> {
                writeName(label, deprecated);
                writeNameAsCode(name, deprecated);
            },
            () -> writeName(name, deprecated)
        );
        // component type
        builder
            .appendSpace().append("[.type]#")
            .append(component.type().name().toLowerCase())
            .append("#");
    }

    private void writeName(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        builder.appendSpace();
        if (deprecated) {
            builder.append("[.line-through]#").append(name).append("#");
        } else {
            builder.append(name);
        }
    }

    private void writeNameAsCode(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        builder.appendSpace();
        if (deprecated) {
            builder.append("[.line-through]#").appendCode(name).append("#");
        } else {
            builder.appendCode(name);
        }
    }

    private void writeDescription(Component component) {
        if (component.isDeprecated()) {
            writeDeprecationNotice(component);
        }
        component.description().ifPresent(description ->
            builder.appendNewLine().appendMarkdownText(description).appendNewLine()
        );
    }

    private void writeDeprecationNotice(Component component) {
        builder.
            appendNewLine()
            .append("*").append(bundle.deprecated()).append("*");
        component.getFirstAnnotation(DocAnnotationType.DEPRECATED).ifPresent(a -> {
            String text = a.value();
            if (!text.isEmpty()) {
                builder.appendSpace().appendMarkdownText(text);
            }
        });
        builder.appendNewLine();
    }

    private void writeColumnAnchor(Column column) {
        String fullName = column.fullName();
        builder.append("[[").append(fullName).append("]]");
    }

    private void writeMetadata(String attribute, Optional<String> metadata) {
        metadata.ifPresent(
            value -> builder.append(attribute).appendSpace().append(value).appendNewLine());
    }
}
