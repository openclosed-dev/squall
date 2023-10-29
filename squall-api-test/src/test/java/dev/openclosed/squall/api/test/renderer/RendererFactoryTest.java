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

package dev.openclosed.squall.api.test.renderer;

import dev.openclosed.squall.api.renderer.RendererFactory;
import dev.openclosed.squall.api.ServiceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RendererFactoryTest {

    @Test
    public void newFactoryShouldThrowServiceException() {
        assertThatThrownBy(() -> {
            RendererFactory.newFactory("unsupported");
        }).isInstanceOf(ServiceException.class)
            .hasMessage("Unable to create requested service [dev.openclosed.squall.api.renderer.RendererFactory]");
    }
}
