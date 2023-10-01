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

import java.util.Optional;

/**
 * A column in table.
 */
public interface Column extends Component, DataType {

    @Override
    default Type type() {
        return Type.COLUMN;
    }

    @Override
    default void accept(SpecVisitor visitor) {
        visitor.visit(this);
    }

    boolean isRequired();

    default boolean isNullable() {
        return !isRequired();
    }

    boolean isPrimaryKey();

    boolean isUnique();

    Optional<Expression> defaultValue();
}
