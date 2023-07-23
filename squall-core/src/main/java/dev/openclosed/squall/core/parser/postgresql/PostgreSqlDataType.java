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

package dev.openclosed.squall.core.parser.postgresql;

import java.util.HashMap;
import java.util.Map;

import dev.openclosed.squall.api.spec.DataType;

enum PostgreSqlDataType implements DataType {
    BIGSERIAL,
    BYTEA,
    JSON,
    JSONB,
    MONEY,
    SMALLSERIAL,
    SERIAL,
    TIMESTAMPTZ,
    TEXT,
    UUID,
    XML;

    private final String typeName;

    PostgreSqlDataType() {
        this.typeName = name().toLowerCase();
    }

    @Override
    public String typeName() {
        return typeName;
    }

    static Map<String, DataType> valuesAsMap() {
        var map = new HashMap<String, DataType>();
        for (var value : values()) {
            map.put(value.typeName(), value);
        }
        return map;
    }
}
