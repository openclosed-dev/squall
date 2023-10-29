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

package dev.openclosed.squall.api.text.json;

import dev.openclosed.squall.api.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Reader of texts in JSON format.
 */
public interface JsonReader {

    /**
     * Reads a JSON text into a map.
     * @param text the text to parse.
     * @return a map generated from the text.
     * @throws JsonReadingException if an error has occurred while reading the text.
     */
    Map<String, ?> readObject(String text) throws JsonReadingException;

    /**
     * Reads a JSON text into an array.
     * @param text the text to parse.
     * @return an array generated from the text.
     * @throws JsonReadingException if an error has occurred while reading the text.
     */
    List<?> readArray(String text) throws JsonReadingException;

    /**
     * Creates an instance of this type.
     * @return newly created JSON reader.
     * @throws ServiceException if an error has occurred while loading the service.
     */
    static JsonReader newReader() {
        try {
            return ServiceLoader.load(JsonReader.class).findFirst().get();
        } catch (Exception e) {
            throw new ServiceException(JsonReader.class, e);
        }
    }
}
