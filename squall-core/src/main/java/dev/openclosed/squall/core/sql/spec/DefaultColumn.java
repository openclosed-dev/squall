/*
 * Copyright 2023 The Squall Authors
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

package dev.openclosed.squall.core.sql.spec;

import dev.openclosed.squall.api.sql.spec.Component;
import dev.openclosed.squall.api.sql.spec.Column;
import dev.openclosed.squall.api.sql.spec.DocAnnotation;
import dev.openclosed.squall.api.sql.expression.Expression;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Default implementation of {@link Column}.
 * @param name
 * @param parents
 * @param typeName
 * @param length
 * @param precision
 * @param scale
 * @param isRequired
 * @param isPrimaryKey
 * @param isUnique
 * @param defaultValue the default value for this column
 * @param annotations
 */
public record DefaultColumn(
        String name,
        List<String> parents,
        String typeName,
        OptionalInt length,
        OptionalInt precision,
        OptionalInt scale,
        boolean isRequired,
        boolean isPrimaryKey,
        boolean isUnique,
        Optional<Expression> defaultValue,
        List<DocAnnotation> annotations
        ) implements Column, BasicComponent {

    @Override
    public Type type() {
        return Component.Type.COLUMN;
    }
}