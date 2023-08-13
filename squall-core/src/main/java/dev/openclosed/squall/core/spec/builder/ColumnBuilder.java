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

package dev.openclosed.squall.core.spec.builder;

import dev.openclosed.squall.api.spec.Column;
import dev.openclosed.squall.api.spec.DataType;
import dev.openclosed.squall.api.spec.DocAnnotation;
import dev.openclosed.squall.api.spec.Expression;
import dev.openclosed.squall.api.spec.Typecast;
import dev.openclosed.squall.core.spec.DefaultColumn;

import java.util.List;
import java.util.Optional;

final class ColumnBuilder extends ComponentBuilder {

    private final DataType dataType;
    private boolean isPrimaryKey = false;
    private boolean isRequired = false;
    private boolean isUnique = false;
    private Expression defaultValue;

    ColumnBuilder(String name, List<String> parents, DataType dataType, List<DocAnnotation> annotations) {
        super(name, parents, annotations);
        this.dataType = dataType;
    }

    Column build() {
        var simplifiedValue = simplifyDefaultValue(this.defaultValue, this.dataType);
        return new DefaultColumn(
            name(),
            parents(),
            dataType.typeName(),
            dataType.length(),
            dataType.precision(),
            dataType.scale(),
            isRequired,
            isPrimaryKey,
            isUnique,
            Optional.ofNullable(simplifiedValue),
            annotations());
    }

    void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
        this.isRequired = true;
    }

    void setRequired(boolean isRequired) {
        if (!isPrimaryKey) {
            this.isRequired = isRequired;
        }
    }

    void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    void setDefaultValue(Expression value) {
        this.defaultValue = value;
    }

    private static Expression simplifyDefaultValue(Expression value, DataType targetType) {
        if (!(value instanceof Typecast typecast)) {
            return value;
        }
        Expression source = typecast.source();
        return switch (source.type()) {
            case NULL -> Expression.NULL;
            case STRING -> {
                if (typecast.typeName().equals(targetType.typeName())
                    && typecast.length().isEmpty()
                    && typecast.precision().isEmpty()) {
                    yield source;
                } else {
                    yield value;
                }
            }
            default -> value;
        };
    }
}
