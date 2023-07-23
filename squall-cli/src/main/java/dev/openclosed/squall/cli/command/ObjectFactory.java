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

package dev.openclosed.squall.cli.command;

import java.util.ServiceLoader;

import dev.openclosed.squall.cli.spi.BaseCommand;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

final class ObjectFactory implements IFactory {

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        if (BaseCommand.class.isAssignableFrom(cls)) {
            @SuppressWarnings("unchecked")
            var subcommand = (K) createSubcommand((Class<? extends BaseCommand>) cls);
            return subcommand;
        }
        return CommandLine.defaultFactory().create(cls);
    }

    private BaseCommand createSubcommand(Class<? extends BaseCommand> cls) {
        var provider = ServiceLoader.load(BaseCommand.class).stream()
                .filter(p -> p.type() == cls)
                .findFirst()
                .orElseThrow();
        return provider.get();
    }
}
