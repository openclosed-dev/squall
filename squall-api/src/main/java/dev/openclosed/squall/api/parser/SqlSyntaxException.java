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

import dev.openclosed.squall.api.base.Location;
import dev.openclosed.squall.api.base.Message;

public final class SqlSyntaxException extends RuntimeException {

    private static final long serialVersionUID = 479698069259089077L;

    private final Message message;
    private final Location location;

    public SqlSyntaxException(Message message, Location location) {
        super(message.get());
        this.message = message;
        this.location = location;
    }

    public Message getBundledMessage() {
        return message;
    }

    public Location getLocation() {
        return location;
    }
}
