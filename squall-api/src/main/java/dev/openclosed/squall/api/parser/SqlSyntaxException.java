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

package dev.openclosed.squall.api.parser;

import dev.openclosed.squall.api.text.Location;
import dev.openclosed.squall.api.message.Message;

/**
 * Exception thrown if an error has occurred while parsing SQL.
 */
public final class SqlSyntaxException extends RuntimeException {

    private static final long serialVersionUID = 479698069259089077L;

    /**
     * Error message.
     */
    private final Message message;
    /**
     * Location where this exception has occurred.
     */
    private final Location location;

    /**
     * Constructs an exception.
     * @param message the message describing the exception.
     * @param location the location where the error has occurred.
     */
    public SqlSyntaxException(Message message, Location location) {
        super(message.get());
        this.message = message;
        this.location = location;
    }

    /**
     * Returns the message describing this exception.
     * @return the message describing this exception.
     */
    public Message getBundledMessage() {
        return message;
    }

    /**
     * Returns the location where this error has occurred.
     * @return the location where this error has occurred.
     */
    public Location getLocation() {
        return location;
    }
}
