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

import java.lang.System.Logger.Level;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import dev.openclosed.squall.api.base.JsonPointer;
import dev.openclosed.squall.api.base.Message;
import dev.openclosed.squall.api.base.Problem;
import dev.openclosed.squall.api.config.MessageBundle;

final class MapperContext {

    private final Consumer<Problem> problemHandler;
    private final MessageBundle messageBundle;
    private final List<String> tokens = new ArrayList<>();
    private final Map<Class<? extends Record>, RecordType<?>> recordCache = new HashMap<>();

    MapperContext(Consumer<Problem> problemHandler, MessageBundle messageBundle) {
        this.problemHandler = problemHandler;
        this.messageBundle = messageBundle;
    }

    @SuppressWarnings("unchecked")
    <T extends Record> RecordType<T> getRecordType(Class<T> target) {
        return (RecordType<T>) recordCache.computeIfAbsent(target, RecordType::of);
    }

    void push() {
        tokens.add(null);
    }

    void pop() {
        tokens.remove(tokens.size() - 1);
    }

    void setCurrent(String name) {
        tokens.set(tokens.size() - 1, name);
    }

    void setCurrent(int index) {
        tokens.set(tokens.size() - 1, String.valueOf(index));
    }

    void addProblem(Level severity, Message message) {
        var problem = new ConfigProblem(severity, message, JsonPointer.of(tokens));
        problemHandler.accept(problem);
    }

    MessageBundle messages() {
        return this.messageBundle;
    }
}
