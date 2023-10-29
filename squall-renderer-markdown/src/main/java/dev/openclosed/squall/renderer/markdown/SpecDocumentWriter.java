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
import java.util.Set;

import dev.openclosed.squall.api.renderer.MessageBundle;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.sql.spec.Column;
import dev.openclosed.squall.api.sql.spec.Component;
import dev.openclosed.squall.api.sql.spec.Database;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.annotation.DocAnnotationType;
import dev.openclosed.squall.api.sql.spec.Schema;
import dev.openclosed.squall.api.sql.spec.Sequence;
import dev.openclosed.squall.api.sql.spec.SpecVisitor;
import dev.openclosed.squall.api.sql.spec.Table;

/**
 * A writer of specification document.
 */
class SpecDocumentWriter implements SpecVisitor, WriterContext {

    private final RenderConfig config;
    private final MessageBundle bundle;
    private final DocBuilder builder;

    private final Set<Component.Type> show;

    private final HeadingNumberGenerator headingNumberGenerator;
    private final TabularComponentWriter<Column> columnWriter;
    private final TabularComponentWriter<Sequence> sequenceWriter;

    private int level;
    private Table currentTable;
    private int databaseCount;

    private static final String[] HEADING_PREFIX = {
        "#", "##", "###", "####", "#####", "######", "#######"
    };

    SpecDocumentWriter(RenderConfig config, MessageBundle bundle, Appendable appendable) {
        this.config = config;
        this.bundle = bundle;
        this.builder = new DocBuilder(appendable);

        this.show = config.show();

        this.headingNumberGenerator = HeadingNumberGenerator.create(config.numbering());

        this.columnWriter = TabularComponentWriter.columnWriter(
            config.columnAttributes(),
            this.builder,
            this,
            this::writeColumnAnchor);
        this.sequenceWriter = TabularComponentWriter.sequenceWriter(
            config.sequenceAttributes(), this.builder, this);
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.level = 0;
        this.currentTable = null;
        this.databaseCount = spec.databases().size();
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

        if (databaseCount > 1) {
            writeHeading(database);
            enterLevel();
        }

        writeDescription(database);
        visitChildren(database);

        if (databaseCount > 1) {
            leaveLevel();
        }
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
        sequenceWriter.writeHeader();
        sequenceWriter.writeDataRow(sequence);
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
            columnWriter.writeHeader();

            visitChildren(table);
        }

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
        builder.append("# ").append(metadata.title()).appendNewLine();

        enterLevel();
    }

    private void finishSpec() {
        leaveLevel();
        writeImageDefinitions();
    }

    private boolean shouldRender(Component.Type type) {
        return this.show.contains(type);
    }

    private void enterLevel() {
        this.level++;
        this.headingNumberGenerator.enterLevel();
    }

    private void leaveLevel() {
        assert this.level > 0;
        this.level--;
        this.headingNumberGenerator.leaveLevel();
    }

    private void writeHeading(Component component) {
        builder
            .appendNewLine()
            .append(HEADING_PREFIX[level])
            .append(this.headingNumberGenerator.generate());

        writeHeadingText(component);

        Badge badge = Badge.mapComponentType(component.type());
        builder
            .appendSpace()
            .append("![").append(badge.label()).append(']').appendNewLine();
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
    }

    private void writeName(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        builder.appendSpace();
        if (deprecated) {
            builder.append("~~").append(name).append("~~");
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
            builder.append("~~`").append(name).append("`~~");
        } else {
            builder.append('`').append(name).append('`');
        }
    }

    private void writeDescription(Component component) {
        if (component.isDeprecated()) {
            writeDeprecationNotice(component);
        }
        component.description().ifPresent(description ->
            builder.appendNewLine().append(description).appendNewLine()
        );
    }

    private void writeDeprecationNotice(Component component) {
        builder.appendNewLine()
            .append("**").append(bundle.deprecated()).append("**");
        component.getFirstAnnotation(DocAnnotationType.DEPRECATED).ifPresent(a -> {
            String text = a.value();
            if (!text.isEmpty()) {
                builder.appendSpace().append(text);
            }
        });
        builder.appendNewLine();
    }

    private void writeImageDefinitions() {
        builder.appendNewLine();
        for (var badge : Badge.values()) {
            builder
                .append('[').append(badge.name()).append("]: ")
                .append(badge.url()).appendNewLine();
        }
    }

    private void writeColumnAnchor(Column column) {
        String fullName = column.fullName();
        builder
            .appendSpace()
            .append("<a id=\"").append(fullName)
            .append("\" name=\"").append(fullName)
            .append("\"></a>");
    }
}
