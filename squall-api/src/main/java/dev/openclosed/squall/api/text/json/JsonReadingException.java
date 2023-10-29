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

import dev.openclosed.squall.api.text.Location;

/**
 * Exception thrown while reading a JSON document.
 */
public class JsonReadingException extends RuntimeException {

    private static final long serialVersionUID = -2756820567524391263L;

    /**
     * Location where this exception has occurred.
     */
    private final Location location;

    /**
     * Constructs an exception.
     * @param cause the cause of the exception.
     * @param location the location where this exception has occurred.
     */
    public JsonReadingException(Throwable cause, Location location) {
        super(cause);
        this.location = location;
    }

    /**
     * Returns the location where this exception has occurred.
     * @return the location where this exception has occurred.
     */
    public final Location getLocation() {
        return this.location;
    }
}
