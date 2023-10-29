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

package dev.openclosed.squall.api.spi;

/**
 * Exception thrown while loading a service.
 */
public class ServiceException extends RuntimeException {

    /**
     * Constructs an exception.
     * @param serviceClass the class of the service to load.
     * @param cause the cause of the exception.
     */
    public ServiceException(Class<?> serviceClass, Throwable cause) {
        super(buildMessage(serviceClass), cause);
    }

    private static String buildMessage(Class<?> serviceClass) {
        return "Unable to create requested service " + '[' + serviceClass.getName() + ']';
    }
}
