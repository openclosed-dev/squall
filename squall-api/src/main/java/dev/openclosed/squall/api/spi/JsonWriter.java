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

package dev.openclosed.squall.api.spi;

import java.util.Map;
import java.util.ServiceLoader;

/**
 * Writer of texts in JSON format.
 */
public interface JsonWriter {

    /**
     * Writes a map into text in JSON format.
     * @param object the map to write.
     * @return generated text in JSON format.
     * @throws JsonWritingException if an error has occurred white writing the text.
     */
    String writeObject(Map<String, ?> object) throws JsonWritingException;

    /**
     * Creates an instance of this type.
     * @return newly created JSON writer.
     */
    static JsonWriter newWriter() {
        return ServiceLoader.load(JsonWriter.class).findFirst().get();
    }
}
