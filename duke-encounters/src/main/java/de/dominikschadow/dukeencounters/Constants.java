/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.dukeencounters;

/**
 * Project wide constants. Utility class with hidden constructor to avoid instantiation.
 *
 * @author Dominik Schadow
 */
public final class Constants {
    /**
     * XSS error code, value is {@value}.
     */
    public static final String XSS_ERROR_CODE = "xss.attempt";
    /**
     * SQL injection error code, value is {@value}.
     */
    public static final String SQLI_ERROR_CODE = "sqli.attempt";
    /**
     * Invalid value error code, value is {@value}.
     */
    public static final String ATTACK_ERROR_CODE = "invalid.value";
    /**
     * Invalid year error code, value is {@value}.
     */
    public static final String INVALID_YEAR_ERROR_CODE = "invalid.year";
    /**
     * Passwords don't match error code, value is {@value}.
     */
    public static final String NOT_MATCHING_PASSWORDS_ERROR_CODE = "password.nomatch";
    /**
     * The new password is not safe, value is {@value}.
     */
    public static final String UNSAFE_PASSWORD_ERROR_CODE = "password.unsafe";
    /**
     * The current password is not correct, value is {@value}.
     */
    public static final String CURRENT_PASSWORD_NOT_CORRECT_ERROR_CODE = "password.incorrect";
    /**
     * The entered username already exists, value is {@value}.
     */
    public static final String USERNAME_ALREADY_EXISTS = "username.exists";
    /**
     * Like for SQL queries, value is {@value}.
     */
    public static final String LIKE = "%";
    /**
     * Year of Java creation, any Duke encounter before this year is impossible, value is {@value}.
     */
    public static final int YEAR_OF_JAVA_CREATION = 1995;

    private Constants() {
    }
}
