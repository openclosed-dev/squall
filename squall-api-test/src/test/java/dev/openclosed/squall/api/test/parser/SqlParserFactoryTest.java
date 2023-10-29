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

package dev.openclosed.squall.api.test.parser;

import dev.openclosed.squall.api.parser.SqlParserFactory;
import dev.openclosed.squall.api.spi.ServiceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SqlParserFactoryTest {

    @Test
    public void newFactoryShouldThrowServiceException() {
        assertThatThrownBy(() -> {
            SqlParserFactory.newFactory("unsupported");
        }).isInstanceOf(ServiceException.class)
            .hasMessage("Unable to create requested service [dev.openclosed.squall.api.parser.SqlParserFactory]");
    }
}
