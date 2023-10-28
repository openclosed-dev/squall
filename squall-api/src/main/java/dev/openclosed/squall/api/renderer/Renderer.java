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

package dev.openclosed.squall.api.renderer;

import java.io.IOException;
import java.nio.file.Path;

import dev.openclosed.squall.api.sql.spec.DatabaseSpec;

/**
 * Renderer for generating database design specification documents.
 */
public interface Renderer {

    /**
     * Renders a document from the input specification.
     * @param spec the database design specification.
     * @param directory the directory where output will be generated.
     * @throws IOException if an I/O error has occurred while rendering the document.
     */
    void render(DatabaseSpec spec, Path directory) throws IOException;
}
