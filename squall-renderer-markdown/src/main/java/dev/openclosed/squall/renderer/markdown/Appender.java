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

interface Appender extends Appendable {

    @Override
    Appender append(CharSequence csq);

    @Override
    Appender append(CharSequence csq, int start, int end);

    @Override
    Appender append(char c);

    default Appender appendSpace() {
        return append(' ');
    }

    default Appender appendNewLine() {
        return append('\n');
    }

    default Appender appendInlineCode(CharSequence csq) {
        if (!csq.isEmpty()) {
            append('`').append(csq).append('`');
        }
        return this;
    }
}
