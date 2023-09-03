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

package dev.openclosed.squall.cli.spi;

import dev.openclosed.squall.api.base.Message;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A bundle of messages for CLI commands.
 */
public interface MessageBundle {

    String BUNDLE_BASE_NAME = "dev.openclosed.squall.cli.Messages";

    /**
     * Creates a message bundle.
     * @param locale the locale of the messages.
     * @return newly created message bundle.
     */
    static MessageBundle forLocale(Locale locale) {
        Objects.requireNonNull(locale);
        var resourceBundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
        return () -> resourceBundle;
    }

    //CHECKSTYLE:OFF

    default Message COMMAND_COMPLETED(String name, long timeElapsed) {
        return of("COMMAND_COMPLETED", name, timeElapsed);
    }

    default Message WORKING_DIRECTORY_NOT_EXIST(Path path) {
        return of("WORKING_DIRECTORY_NOT_EXIST", path.toString());
    }

    default Message WORKING_DIRECTORY_IS_NOT_DIRECTORY(Path path) {
        return of("WORKING_DIRECTORY_IS_NOT_DIRECTORY", path.toString());
    }

    default Message FAILED_TO_READ_FILE(Path path) {
        return of("FAILED_TO_READ_FILE", path.toString());
    }

    default Message FAILED_TO_WRITE_FILE(Path path) {
        return of("FAILED_TO_WRITE_FILE", path.toString());
    }

    default Message CANNOT_TO_CREATE_OUTPUT_DIRECTORY(Path path) {
        return of("CANNOT_TO_CREATE_OUTPUT_DIRECTORY", path.toString());
    }

    default Message CONFIGURATION_ALREADY_EXISTS(Path path) {
        return of("CONFIGURATION_ALREADY_EXISTS", path.toString());
    }

    default Message LOADING_CONFIGURATION(Path path) {
        return of("LOADING_CONFIGURATION", path.toString());
    }

    default Message LOADED_CONFIGURATION() {
        return of("LOADED_CONFIGURATION");
    }

    default Message CONFIGURATION_NOT_EXIST(Path path) {
        return of("CONFIGURATION_NOT_EXIST", path.toString());
    }

    default Message CONFIGURATION_INVALID() {
        return of("CONFIGURATION_INVALID");
    }

    default Message NO_SQL_SOURCES() {
        return of("NO_SQL_SOURCES");
    }

    default Message SQL_FILE_NOT_EXIST(Path path) {
        return of("SQL_FILE_NOT_EXIST", path.toString());
    }

    // init command

    default Message INITIALIZED_SUCCESSFULLY(Path path) {
        return of("INITIALIZED_SUCCESSFULLY", path.toString());
    }

    // render command

    default Message NO_RENDERER_DEFINED() {
        return of("NO_RENDERER_DEFINED");
    }

    default Message RENDERER_NOT_DEFINED(String name) {
        return of("RENDERER_NOT_DEFINED", name);
    }

    default Message RENDERER_UNAVAILABLE(String format) {
        return of("RENDERER_UNAVAILABLE", format);
    }

    default Message PARSING_SQL_SOURCE(String path) {
        return of("PARSING_SQL_SOURCE", path);
    }

    default Message PARSED_SQL_SOURCE(String path) {
        return of("PARSED_SQL_SOURCE", path);
    }

    default Message FOUND_SQL_ERRORS(int files) {
        return of("FOUND_SQL_ERRORS", files);
    }

    default Message RENDERING_SPEC(String rendererName, Path path) {
        return of("RENDERING_SPEC", rendererName, path.toString());
    }

    default Message RENDERED_SPEC(String rendererName, Path path) {
        return of("RENDERED_SPEC", rendererName, path.toString());
    }

    //CHECKSTYLE:ON

    ResourceBundle getResourceBundle();

    private Message of(String key, Object... args) {
        return Message.of(key, args, getResourceBundle());
    }
}
