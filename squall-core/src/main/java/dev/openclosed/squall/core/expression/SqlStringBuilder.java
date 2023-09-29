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

package dev.openclosed.squall.core.expression;

import dev.openclosed.squall.api.expression.Expression;

final class SqlStringBuilder implements Appendable {

    private final StringBuilder builder = new StringBuilder();

    @Override
    public SqlStringBuilder append(CharSequence csq) {
        builder.append(csq);
        return this;
    }

    @Override
    public SqlStringBuilder append(CharSequence csq, int start, int end) {
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public SqlStringBuilder append(char c) {
        builder.append(c);
        return this;
    }

    SqlStringBuilder append(int i) {
        builder.append(i);
        return this;
    }

    SqlStringBuilder append(Expression expression) {
        return append(expression.toSql());
    }

    SqlStringBuilder appendGroupedIfComplex(Expression expression) {
        if (expression.isComplex()) {
            return append('(').append(expression.toSql()).append(')');
        } else {
            return append(expression);
        }
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
