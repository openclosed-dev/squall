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

package dev.openclosed.squall.core.sql.spec;

import dev.openclosed.squall.api.sql.spec.ComponentOrder;
import dev.openclosed.squall.api.sql.spec.Database;
import dev.openclosed.squall.api.sql.spec.DatabaseSpec;
import dev.openclosed.squall.api.sql.spec.SpecMetadata;
import dev.openclosed.squall.api.sql.spec.SpecVisitor;
import dev.openclosed.squall.core.base.RecordMapSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record DefaultDatabaseSpec(
        Optional<SpecMetadata> metadata,
        List<Database> databases
        ) implements DatabaseSpec, RecordMapSource {



    @Override
    public SpecMetadata getMetadataOrDefault() {
        return metadata().orElse(DefaultSpecMetadata.getDefault());
    }

    @Override
    public void walkSpec(ComponentOrder order, SpecVisitor visitor) {
        Objects.requireNonNull(visitor);
        databases().stream().forEach(c -> c.accept(visitor));
    }
}