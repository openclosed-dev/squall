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

import dev.openclosed.squall.api.renderer.RenderConfig;
import dev.openclosed.squall.api.renderer.support.DelegatingAppender;
import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.Database;
import dev.openclosed.squall.api.spec.DatabaseSpec;
import dev.openclosed.squall.api.spec.Schema;
import dev.openclosed.squall.api.spec.SpecVisitor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ResourceBundle;

final class SpecWriter implements SpecVisitor, DelegatingAppender {

    private final RenderConfig config;
    private final ResourceBundle bundle;
    private final Appendable appender;

    private int level;

    private static final String[] HEADING_PREFIX = {
        "=", "==", "===", "====", "=====", "======", "======="
    };

    SpecWriter(RenderConfig config, ResourceBundle bundle, Appendable appender) {
        this.config = config;
        this.bundle = bundle;
        this.appender = appender;
    }

    void writeSpec(DatabaseSpec spec) throws IOException {
        this.level = 0;
        try {
            spec.walkSpec(this, config.order());
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    // SpecVisitor

    @Override
    public void visit(DatabaseSpec spec, Context context) {
        String title = spec.title().orElse("Untitled");
        append("= ").append(title).appendNewLine();

        enterLevel();
    }

    @Override
    public void leave(DatabaseSpec spec) {
        leaveLevel();
    }

    @Override
    public void visit(Database database, int ordinal, Context context) {
        writeDescription(database);
    }

    @Override
    public void leave(Database database) {
    }

    @Override
    public void visit(Schema schema, int ordinal, Context context) {
        writeHeading(schema);
        enterLevel();

        writeDescription(schema);
    }

    @Override
    public void leave(Schema schema) {
        leaveLevel();
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
        append(HEADING_PREFIX[level]);

        writeHeadingText(component);
        appendNewLine();
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
            append("~~`").append(name).append("`~~");
        } else {
            append("`+").append(name).append("+`");
        }
    }

    private void writeDescription(Component component) {
        component.description().ifPresent(description ->
            appendNewLine().append(description).appendNewLine()
        );
    }
}
