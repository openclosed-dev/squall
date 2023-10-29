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

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.text.json.JsonReader;
import dev.openclosed.squall.api.text.json.JsonReadingException;

/**
 * An implementation of {@link JsonReader}.
 */
public final class JacksonJsonReader implements JsonReader {

    private final ObjectMapper objectMapper = buildObjectMapper();

    private static final TypeReference<Map<String, Object>> MAP_TYPE
        = new TypeReference<Map<String, Object>>() { };

    private static final TypeReference<List<Object>> LIST_TYPE
        = new TypeReference<List<Object>>() { };

    @Override
    public Map<String, Object> readObject(String text) throws JsonReadingException {
        return read(text, MAP_TYPE);
    }

    @Override
    public List<Object> readArray(String text) throws JsonReadingException {
        return read(text, LIST_TYPE);
    }

    private <T> T read(String text, TypeReference<T> type) throws JsonReadingException {
        Objects.requireNonNull(text);
        try {
            return objectMapper.readValue(text, type);
        } catch (JsonEOFException | MismatchedInputException e) {
            JsonLocation loc = e.getLocation();
            throw new JsonReadingException(e,
                    new Location(loc.getLineNr(), loc.getColumnNr(), loc.getCharOffset()));
        } catch (JsonProcessingException e) {
            JsonLocation loc = e.getLocation();
            throw new JsonReadingException(e,
                    new Location(loc.getLineNr(), loc.getColumnNr() - 1, loc.getCharOffset()));
        }
    }

    private static ObjectMapper buildObjectMapper() {
        return JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
                .build();
    }
}
