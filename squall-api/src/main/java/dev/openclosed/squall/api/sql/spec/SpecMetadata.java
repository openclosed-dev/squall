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

import java.util.Objects;
import java.util.Optional;

/**
 * Metadata of specification.
 * @param title the title of the specification.
 * @param author the author of the specification.
 * @param version the version of the specification.
 * @param date the release date.
 */
public record SpecMetadata(
    String title,
    Optional<String> author,
    Optional<String> version,
    Optional<String> date) {

    public static final String DEFAULT_TITLE = "Untitled";

    /**
     * {@code SpecMetadata} with default values.
     */
    public static final SpecMetadata DEFAULT = new SpecMetadata();

    /**
     * Creates an instance of a {@code SpecMetadata} record class filled with default values.
     */
    public SpecMetadata() {
        this(DEFAULT_TITLE, Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Creates an instance of a {@code SpecMetadata} record class.
     * @param title the title of the specification, can be {@code null}.
     * @param author the author of the specification, or empty.
     * @param version the version of the specification, or empty.
     * @param date the release date, or empty.
     */
    public SpecMetadata {
        Objects.requireNonNull(author);
        Objects.requireNonNull(version);
        Objects.requireNonNull(date);

        if (title == null) {
            title = DEFAULT_TITLE;
        }
    }
}
