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

package dev.openclosed.squall.core.config;

import java.util.Optional;
import java.util.function.Function;

import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.config.MessageBundle;

final class MappingException extends RuntimeException {

    private static final long serialVersionUID = -9081164663612375584L;

    private final Function<MessageBundle, Message> messageProvider;

    MappingException() {
        this.messageProvider = bundle -> null;
    }

    MappingException(Function<MessageBundle, Message> messageProvider) {
        this.messageProvider = messageProvider;
    }

    MappingException(Throwable cause) {
        super(cause);
        this.messageProvider = bundle -> null;
    }

    Optional<Message> getMessage(MessageBundle bundle) {
        return Optional.ofNullable(messageProvider.apply(bundle));
    }
}
