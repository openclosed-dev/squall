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

package dev.openclosed.squall.service.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dev.openclosed.squall.api.spi.JsonWriter;
import dev.openclosed.squall.api.spi.JsonWritingException;

import java.util.Map;
import java.util.Objects;

public class JacksonJsonWriter implements JsonWriter {

    private final ObjectWriter objectWriter = buildObjectMapper();

    @Override
    public String writeObject(Map<String, ?> object) throws JsonWritingException {
        Objects.requireNonNull(object);
        try {
            return objectWriter.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonWritingException(e);
        }
    }

    private static ObjectWriter buildObjectMapper() {
        return JsonMapper.builder()
            .build()
            .writerWithDefaultPrettyPrinter();
    }
}
