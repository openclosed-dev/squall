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

package dev.openclosed.squall.api.sql.expression;

import dev.openclosed.squall.api.base.Property;

/**
 * Boolean constants.
 * @param value the constant value.
 */
public record BooleanConstant(
    @Property(value = "value", omit = false) boolean value)
    implements Expression {

    /**
     * TRUE constant.
     */
    public static final BooleanConstant TRUE = new BooleanConstant(true);

    /**
     * FALSE constant.
     */
    public static final BooleanConstant FALSE = new BooleanConstant(false);

    @Override
    public Type type() {
        return Type.BOOLEAN;
    }

    @Override
    public String toSql() {
        return value() ? "true" : "false";
    }
}