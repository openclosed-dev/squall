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
import dev.openclosed.squall.core.spec.DefaultColumn;

import java.util.List;
import java.util.Optional;

final class ColumnBuilder extends ComponentBuilder {

    private final DataType dataType;
    private boolean nullable = true;
    private boolean unique = false;
    private Expression defaultValue;

    ColumnBuilder(String name, DataType dataType, List<DocAnnotation> annotations) {
        super(name, annotations);
        this.dataType = dataType;
    }

    Column build() {
        return new DefaultColumn(
            name(),
            dataType.typeName(),
            dataType.length(),
            dataType.precision(),
            dataType.scale(),
            nullable,
            unique,
            Optional.ofNullable(defaultValue),
            annotations());
    }

    void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    void setDefaultValue(Expression defaultValue) {
        this.defaultValue = defaultValue;
    }

    void setUnique(boolean unique) {
        this.unique = unique;
    }
}
