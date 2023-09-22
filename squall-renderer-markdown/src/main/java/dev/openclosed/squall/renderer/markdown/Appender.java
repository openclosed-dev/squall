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

package dev.openclosed.squall.renderer.markdown;

import java.io.IOException;
import java.io.UncheckedIOException;

final class Appender implements Appendable {

    private final Appendable appendable;

    Appender(Appendable appendable) {
        this.appendable = appendable;
    }

    @Override
    public Appender append(CharSequence csq) {
        try {
            appendable.append(csq);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public Appender append(CharSequence csq, int start, int end) {
        try {
            appendable.append(csq, start, end);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public Appender append(char c) {
        try {
            appendable.append(c);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    Appender appendSpace() {
        return append(' ');
    }

    Appender appendNewLine() {
        return append('\n');
    }
}
