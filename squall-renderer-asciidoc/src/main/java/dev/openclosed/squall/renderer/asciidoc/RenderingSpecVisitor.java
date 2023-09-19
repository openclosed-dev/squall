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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

final class RenderingSpecVisitor implements SpecVisitor, DelegatingAppender {

    private final RenderConfig config;
    private final MessageBundle bundle;
    private final Appendable appender;

    private final AsciiDocTableWriter<Column> columnWriter;
    private final AsciiDocTableWriter<Sequence> sequenceWriter;

    private int level;

    private static final String[] HEADING_PREFIX = {
        "=", "==", "===", "====", "=====", "======", "======="
    };

    RenderingSpecVisitor(RenderConfig config, MessageBundle bundle, Appendable appender) {
        this.config = config;
        this.bundle = bundle;
        this.appender = appender;
        this.columnWriter = AsciiDocTableWriter.forColumn(
            config.columnAttributes(), bundle, this::writeColumnAnchor);
        this.sequenceWriter = AsciiDocTableWriter.forSequence(config.sequenceAttributes(), bundle);
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.level = 0;
        try {
            spec.walkSpec(config.order(), config.show(), this);
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    // SpecVisitor

    @Override
    public void visit(DatabaseSpec spec, Context context) {
        var metadata = spec.getMetadataOrDefault();
        append("= ").append(metadata.title()).appendNewLine();

        writeMetadata(":author:", metadata.author());
        writeMetadata(":revnumber:", metadata.version());
        writeMetadata(":revdate:", metadata.date());

        append(":icons: font").appendNewLine();
        append(":icon-set: fas").appendNewLine();

        enterLevel();
    }

    @Override
    public void leave(DatabaseSpec spec) {
        leaveLevel();
    }

    @Override
    public void visit(Database database, Context context) {
        writeDescription(database);
    }

    @Override
    public void leave(Database database) {
    }

    @Override
    public void visit(Schema schema, Context context) {
        writeHeading(schema);
        enterLevel();

        writeDescription(schema);
    }

    @Override
    public void leave(Schema schema) {
        leaveLevel();
    }

    @Override
    public void visit(Sequence sequence, Context context) {
        writeHeading(sequence);
        writeDescription(sequence);

        appendNewLine();
        this.sequenceWriter.writeHeader(this);
        this.sequenceWriter.writeDataRow(this, sequence, context);
        this.sequenceWriter.writeFooter(this);
    }

    @Override
    public void visit(Table table, Context context) {
        writeHeading(table);
        writeDescription(table);

        if (table.hasColumns()) {
            appendNewLine();
            this.columnWriter.writeHeader(this);
        }
    }

    @Override
    public void leave(Table table) {
        this.columnWriter.writeFooter(this);
    }

    @Override
    public void visit(Column column, Context context) {
        this.columnWriter.writeDataRow(this, column, context);
    }

    // DelegatingAppender

    @Override
    public Appendable getDelegate() {
        return appender;
    }

    //

    private void enterLevel() {
        this.level++;
    }

    private void leaveLevel() {
        assert this.level > 0;
        this.level--;
    }

    private void writeHeading(Component component) {
        appendNewLine();
        writeSectionAnchor(component);
        append(HEADING_PREFIX[level]);

        writeHeadingText(component);
        appendNewLine();
    }

    private void writeSectionAnchor(Component component) {
        append("[id=").append(component.fullName()).append(']').appendNewLine();
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
        appendSpace().append("[.type]#")
            .append(component.type().name().toLowerCase())
            .append("#");
    }

    private void writeName(String name, boolean deprecated) {
        if (name.isEmpty()) {
            return;
        }
        appendSpace();
        if (deprecated) {
            append("[.line-through]#").append(name).append("#");
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
            append("[.line-through]#`").append(name).append("`#");
        } else {
            append('`').append(name).append('`');
        }
    }

    private void writeDescription(Component component) {
        if (component.isDeprecated()) {
            writeDeprecationNotice(component);
        }
        component.description().ifPresent(description ->
            appendNewLine().append(description).appendNewLine()
        );
    }

    private void writeDeprecationNotice(Component component) {
        appendNewLine();
        append("*").append(bundle.deprecated()).append("*");
        component.getFirstAnnotation(DocAnnotationType.DEPRECATED).ifPresent(a -> {
            String text = a.value();
            if (!text.isEmpty()) {
                appendSpace().append(text);
            }
        });
        appendNewLine();
    }

    private void writeColumnAnchor(Column column) {
        String fullName = column.fullName();
        append("[[").append(fullName).append("]]");
    }

    private void writeMetadata(String attribute, Optional<String> metadata) {
        metadata.ifPresent(
            value -> append(attribute).appendSpace().append(value).appendNewLine());
    }
}
