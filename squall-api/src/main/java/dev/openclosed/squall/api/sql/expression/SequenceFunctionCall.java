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

package dev.openclosed.squall.api.sql.expression;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A sequence manipulation function.
 */
public interface SequenceFunctionCall extends FunctionCall {

    /**
     * Returns all components of sequence name.
     * @return all components of sequence name.
     */
    List<String> sequenceName();

    /**
     * Returns the name of the sequence.
     * @return the name of the sequence.
     */
    default String simpleSequenceName() {
        var name = sequenceName();
        return name.get(name.size() - 1);
    }

    /**
     * Returns the fully qualified name of the sequence.
     * @return the fully qualified name of the sequence.
     */
    default String fullSequenceName() {
        return sequenceName().stream().collect(Collectors.joining("."));
    }
}
