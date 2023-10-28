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

package dev.openclosed.squall.api.sql.spec;

import dev.openclosed.squall.api.sql.datatype.DataType;
import dev.openclosed.squall.api.sql.expression.Expression;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Column in a table.
 * @param name the name of this column.
 * @param parents the parent components of this column.
 * @param typeName the name of the data type.
 * @param length the length of the data type.
 * @param precision the precision of the data type.
 * @param scale the scale of the data type.
 * @param isRequired {@code true} if the value is required for this column.
 * @param isPrimaryKey  {@code true} if this column is one of primary keys.
 * @param isUnique {@code true} if the value must be unique.
 * @param defaultValue the default value for this column.
 * @param annotations the annotations attached to this column.
 */
public record Column(
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
    )
    implements Component, DataType {

    /**
     * Constructs the column.
     * @param name the name of this column.
     * @param parents the parent components of this column.
     * @param typeName the name of the data type.
     * @param length the length of the data type.
     * @param precision the precision of the data type.
     * @param scale the scale of the data type.
     * @param isRequired {@code true} if the value is required for this column.
     * @param isPrimaryKey  {@code true} if this column is one of primary keys.
     * @param isUnique {@code true} if the value must be unique.
     * @param defaultValue the default value for this column.
     * @param annotations the annotations attached to this column.
     */
    public Column {
        Objects.requireNonNull(name);
        Objects.requireNonNull(parents);
        Objects.requireNonNull(typeName);
        Objects.requireNonNull(length);
        Objects.requireNonNull(precision);
        Objects.requireNonNull(scale);
        Objects.requireNonNull(defaultValue);
        Objects.requireNonNull(annotations);
        parents = List.copyOf(parents);
        annotations = List.copyOf(annotations);
    }

    @Override
    public Type type() {
        return Type.COLUMN;
    }

    @Override
    public void accept(SpecVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns whether the value is nullable or not.
     * @return {@code true} if null is allowed.
     */
    public boolean isNullable() {
        return !isRequired();
    }
}
