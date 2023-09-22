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

package dev.openclosed.squall.renderer.markdown;

import java.util.ArrayDeque;
import java.util.Deque;

interface HeadingNumberGenerator {

    HeadingNumberGenerator EMPTY = new HeadingNumberGenerator() { };

    default void enterLevel() {
    }

    default void leaveLevel() {
    }

    default String generate() {
        return "";
    }

    static HeadingNumberGenerator create(boolean enabled) {
        if (enabled) {
            return new HeadingNumberGeneratorImpl();
        } else {
            return EMPTY;
        }
    }
}

final class HeadingNumberGeneratorImpl implements HeadingNumberGenerator {

    private final StringBuilder stringBuilder = new StringBuilder();
    private final Deque<Level> levels = new ArrayDeque<>();

    HeadingNumberGeneratorImpl() {
        stringBuilder.append(' ');
    }

    @Override
    public void enterLevel() {
        this.levels.addLast(new Level(stringBuilder.length(), 1));
    }

    @Override
    public void leaveLevel() {
        this.levels.removeLast();
    }

    @Override
    public String generate() {
        Level level = this.levels.getLast();
        stringBuilder.setLength(level.baseLength());
        stringBuilder.append(level.nextOrdinal()).append('.');
        return stringBuilder.toString();
    }

    private static class Level {

        private final int baseLength;
        private int ordinal;

        Level(int baseLength, int start) {
            this.baseLength = baseLength;
            this.ordinal = start;
        }

        int baseLength() {
            return this.baseLength;
        }

        int nextOrdinal() {
            return this.ordinal++;
        }
    }
}
