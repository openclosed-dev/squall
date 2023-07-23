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

package dev.openclosed.squall.cli.base;

import static dev.openclosed.squall.cli.base.BundledMessage.of;

import java.nio.file.Path;

import dev.openclosed.squall.api.base.Message;

/**
 * Messages for the CLI application.
 */
public final class Messages {

    //CHECKSTYLE:OFF

    public static Message COMMAND_COMPLETED(String name, long timeElapsed) {
        return of("COMMAND_COMPLETED", name, timeElapsed);
    }

    public static Message WORKING_DIRECTORY_NOT_EXIST(Path path) {
        return of("WORKING_DIRECTORY_NOT_EXIST", path.toString());
    }

    public static Message WORKING_DIRECTORY_IS_NOT_DIRECTORY(Path path) {
        return of("WORKING_DIRECTORY_IS_NOT_DIRECTORY", path.toString());
    }

    public static Message FAILED_TO_READ_FILE(Path path) {
        return of("FAILED_TO_READ_FILE", path.toString());
    }

    public static Message FAILED_TO_WRITE_FILE(Path path) {
        return of("FAILED_TO_WRITE_FILE", path.toString());
    }

    public static Message CANNOT_TO_CREATE_OUTPUT_DIRECTORY(Path path) {
        return of("CANNOT_TO_CREATE_OUTPUT_DIRECTORY", path.toString());
    }

    public static Message CONFIGURATION_ALREADY_EXISTS(Path path) {
        return of("CONFIGURATION_ALREADY_EXISTS", path.toString());
    }

    public static Message LOADING_CONFIGURATION(Path path) {
        return of("LOADING_CONFIGURATION", path.toString());
    }

    public static Message LOADED_CONFIGURATION() {
        return of("LOADED_CONFIGURATION");
    }

    public static Message CONFIGURATION_NOT_EXIST(Path path) {
        return of("CONFIGURATION_NOT_EXIST", path.toString());
    }

    public static Message CONFIGURATION_INVALID() {
        return of("CONFIGURATION_INVALID");
    }

    public static Message NO_SQL_SOURCES() {
        return of("NO_SQL_SOURCES");
    }

    public static Message SQL_FILE_NOT_EXIST(Path path) {
        return of("SQL_FILE_NOT_EXIST", path.toString());
    }

    // init command

    public static Message INITIALIZED_SUCCESSFULLY(Path path) {
        return of("INITIALIZED_SUCCESSFULLY", path.toString());
    }

    // render command

    public static Message NO_RENDERER_DEFINED() {
        return of("NO_RENDERER_DEFINED");
    }

    public static Message RENDERER_NOT_DEFINED(String name) {
        return of("RENDERER_NOT_DEFINED", name);
    }

    public static Message RENDERER_UNAVAILABLE(String format) {
        return of("RENDERER_UNAVAILABLE", format);
    }

    public static Message PARSING_SQL_SOURCE(String path) {
        return of("PARSING_SQL_SOURCE", path);
    }

    public static Message PARSED_SQL_SOURCE(String path) {
        return of("PARSED_SQL_SOURCE", path);
    }

    public static Message FOUND_SQL_ERRORS(int files) {
        return of("FOUND_SQL_ERRORS", files);
    }

    public static Message RENDERING_SPEC(String rendererName, Path path) {
        return of("RENDERING_SPEC", rendererName, path.toString());
    }

    public static Message RENDERED_SPEC(String rendererName, Path path) {
        return of("RENDERED_SPEC", rendererName, path.toString());
    }

    //CHECKSTYLE:ON

    private Messages() {
    }
}
