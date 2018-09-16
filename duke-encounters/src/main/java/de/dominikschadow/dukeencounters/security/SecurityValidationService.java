/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.dukeencounters.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

/**
 * Searches for different attack strings with incomplete (and simple!) blacklists. Detects some Cross-Site Scripting
 * (XSS) and SQL Injection attacks.
 *
 * @author Dominik Schadow
 */
@Service
public class SecurityValidationService {
    public final boolean hasXssPayload(@NotNull final String payload) {
        return containsAnyIgnoreCase(payload, "<", "script", "onload", "eval", "document.cookie");
    }

    public final boolean hasSqlIPayload(@NotNull final String payload) {
        return containsAnyIgnoreCase(payload, "drop", "insert", "update", "delete", "union", "select", "exec", "fetch",
                "' or '1'='1", "' or 1=1");
    }

    private boolean containsAnyIgnoreCase(final String payload, final String... searchStrings) {
        for (String searchString : searchStrings) {
            if (StringUtils.containsIgnoreCase(payload, searchString)) {
                return true;
            }
        }

        return false;
    }
}
