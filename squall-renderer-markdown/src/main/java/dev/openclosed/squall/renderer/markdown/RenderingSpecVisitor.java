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

import dev.openclosed.squall.api.renderer.MessageBundle;
import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.support.DelegatingAppender;
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
 * Spec visitor to render document.
 */
class RenderingSpecVisitor implements SpecVisitor, DelegatingAppender {

    private final RenderConfig config;
    private final MessageBundle bundle;
    private final Appendable appender;

    private final HeadingNumberGenerator headingNumberGenerator;
    private final MarkdownTableWriter<Column> columnWriter;
    private final MarkdownTableWriter<Sequence> sequenceWriter;

    private int level;
    private int databaseCount;

    private static final String[] HEADING_PREFIX = {
        "#", "##", "###", "####", "#####", "######", "#######"
    };

    RenderingSpecVisitor(RenderConfig config, MessageBundle bundle, Appendable appender) {
        this.config = config;
        this.bundle = bundle;
        this.appender = appender;

        this.headingNumberGenerator = HeadingNumberGenerator.create(config.numbering());
        this.columnWriter = MarkdownTableWriter.forColumn(
            config.columnAttributes(),
            bundle,
            this::writeColumnAnchor);
        this.sequenceWriter = MarkdownTableWriter.forSequence(config.sequenceAttributes(), bundle);
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.level = 0;
        this.databaseCount = spec.databases().size();
        try {
            spec.walkSpec(config.order(), config.show(), this);
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
    public void visit(DatabaseSpec spec, Context context) {
        String title = spec.title().orElse("Untitled");
        append("# ").append(title).appendNewLine();

        enterLevel();
    }

    @Override
    public void leave(DatabaseSpec spec) {
        leaveLevel();
        writeImageDefinitions();
    }

    @Override
    public void visit(Database database, Context context) {
        if (databaseCount > 1) {
            writeHeading(database);
            enterLevel();
        }

        writeDescriptionSection(database);
    }

    @Override
    public void leave(Database database) {
        if (databaseCount > 1) {
            leaveLevel();
        }
    }

    @Override
    public void visit(Schema schema, Context context) {
        writeHeading(schema);
        enterLevel();

        writeDescriptionSection(schema);
    }

    @Override
    public void leave(Schema schema) {
        leaveLevel();
    }

    @Override
    public void visit(Sequence sequence, Context context) {
        writeHeading(sequence);
        writeDescriptionSection(sequence);

        appendNewLine();
        sequenceWriter.writeHeaderRow(this);
        sequenceWriter.writeDelimiterRow(this);
        sequenceWriter.writeDataRow(this, sequence, context);
    }

    @Override
    public void visit(Table table, Context context) {
        writeHeading(table);
        writeDescriptionSection(table);

        if (table.hasColumns()) {
            appendNewLine();
            columnWriter.writeHeaderRow(this);
            columnWriter.writeDelimiterRow(this);
        }
    }

    @Override
    public void visit(Column column, Context context) {
        this.columnWriter.writeDataRow(this, column, context);
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
        appendNewLine();
        append(HEADING_PREFIX[level]);

        this.headingNumberGenerator.generate(this);

        writeHeadingText(component);

        Badge badge = Badge.mapComponentType(component.type());
        appendSpace();
        append("![").append(badge.label()).append(']').appendNewLine();
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
        append("**").append(bundle.deprecated()).append("**");
        component.getFirstAnnotation(DocAnnotationType.DEPRECATED).ifPresent(a -> {
            String text = a.value();
            if (!text.isEmpty()) {
                appendSpace().append(text);
            }
        });
        appendNewLine();
    }

    private void writeImageDefinitions() {
        appendNewLine();
        for (var badge : Badge.values()) {
            append('[').append(badge.name()).append("]: ");
            append(badge.url()).appendNewLine();
        }
    }

    private void writeColumnAnchor(Column column) {
        String fullName = column.fullName();
        appendSpace();
        append("<a id=\"").append(fullName);
        append("\" name=\"").append(fullName);
        append("\"></a>");
    }
}
