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

package dev.openclosed.squall.api.renderer;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import dev.openclosed.squall.api.spec.Component;
import dev.openclosed.squall.api.spec.ComponentOrder;

/**
 * A configuration for renderers.
 * @param format the output file format.
 * @param basename the basename of the file.
 * @param locale the locale used while rendering messages.
 * @param numbering whether if numbering is enabled or not.
 * @param order the rendering order of components.
 * @param show the components to render.
 * @param columnAttributes the attributes of columns to be rendered.
 * @param sequenceAttributes the attributes of sequences be rendered.
 * @param pageSize the size of the page. Used for PDF format.
 * @param pageOrientation the orientation of the page. Used for PDF format.
 * @param pageMargin the margin of the page. Used for PDF format.
 */
public record RenderConfig(
        String format,
        String basename,
        Locale locale,
        boolean numbering,
        ComponentOrder order,
        Set<Component.Type> show,
        List<ColumnAttribute> columnAttributes,
        List<SequenceAttribute> sequenceAttributes,
        String pageSize,
        PageOrientation pageOrientation,
        List<String> pageMargin) {

    private static final RenderConfig DEFAULT = new RenderConfig();

    public static RenderConfig getDefault() {
        return DEFAULT;
    }

    public RenderConfig() {
        this("markdown",
            "spec",
            Locale.ENGLISH,
            true,
            ComponentOrder.NAME,
            Component.Type.all(),
            ColumnAttribute.defaultList(),
            SequenceAttribute.defaultList(),
            "a4",
            PageOrientation.PORTRAIT,
            List.of("10mm", "10mm", "10mm", "10mm")
        );
    }

    public RenderConfig {
        Objects.requireNonNull(pageMargin);
        if (pageMargin.isEmpty() || pageMargin.size() > 4) {
            throw new IllegalArgumentException();
        }
        pageMargin = List.copyOf(pageMargin);
    }
}
