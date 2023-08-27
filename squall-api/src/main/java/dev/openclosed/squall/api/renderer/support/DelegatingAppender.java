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

package dev.openclosed.squall.api.renderer.support;

import java.io.IOException;
import java.io.UncheckedIOException;

public interface DelegatingAppender extends Appender {

    Appendable getDelegate();

    @Override
    default Appender append(CharSequence csq) {
        try {
            getDelegate().append(csq);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    default Appender append(CharSequence csq, int start, int end) {
        try {
            getDelegate().append(csq, start, end);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    default Appender append(char c) {
        try {
            getDelegate().append(c);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }
}
