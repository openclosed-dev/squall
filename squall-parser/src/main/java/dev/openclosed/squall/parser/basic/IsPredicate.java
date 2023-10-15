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

package dev.openclosed.squall.parser.basic;

import dev.openclosed.squall.api.sql.expression.Expression;
import dev.openclosed.squall.api.sql.expression.Is;

public enum IsPredicate {
    IS_NULL,
    IS_NOT_NULL(IS_NULL),
    IS_TRUE,
    IS_NOT_TRUE(IS_TRUE),
    IS_FALSE,
    IS_NOT_FALSE(IS_FALSE),
    IS_UNKNOWN,
    IS_NOT_UNKNOWN(IS_UNKNOWN);

    private IsPredicate negated;

    IsPredicate() {
        this.negated = null;
    }

    IsPredicate(IsPredicate negated) {
        this.negated = negated;
        negated.negated = this;
    }

    IsPredicate negated() {
        return negated;
    }

    public Expression toExpression(Expression subject) {
        return new Is(subject, name());
    }
}
