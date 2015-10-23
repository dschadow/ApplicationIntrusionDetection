/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Application Intrusion Detection project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dominikschadow.duke.encounters;

/**
 * Project wide constants. Utility class with hidden constructor to avoid instantiation.
 *
 * @author Dominik Schadow
 */
public class Constants {
    /**
     * Error message for XSS payload, value is {@value}.
     */
    public static final String XSS_ERROR_MESSAGE = "This application is XSS bulletproof!";
    /**
     * Error message for SQL injection payload, value is {@value}.
     */
    public static final String SQLI_ERROR_MESSAGE = "This application is SQL Injection bulletproof!";
    /**
     * Error message for not matching passwords (password and confirmation), value is {@value}.
     */
    public static final String PASSWORD_MATCH_ERROR_MESSAGE = "The entered passwords don't match";
    /**
     * XSS error code, value is {@value}.
     */
    public static final String XSS_ERROR_CODE = "xss.attempt";
    /**
     * SQL injection error code, value is {@value}.
     */
    public static final String SQLI_ERROR_CODE = "sqli.attempt";
    /**
     * General error code, value is {@value}.
     */
    public static final String ATTACK_ERROR_CODE = "attack.attempt";
    /**
     * Passwords don't match error code, value is {@value}.
     */
    public static final String PASSWORD_MATCH_ERROR_CODE = "error.passwordmatch";

    private Constants() {
    }
}
