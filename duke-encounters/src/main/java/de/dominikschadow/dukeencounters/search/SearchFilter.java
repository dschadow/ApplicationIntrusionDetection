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
package de.dominikschadow.dukeencounters.search;

import com.google.common.base.Strings;
import lombok.Data;

/**
 * SearchFilter to search for Duke encounters based on various fields.
 *
 * @author Dominik Schadow
 */
@Data
public class SearchFilter {
    private String event;
    private String location;
    private String country;
    private String year;
    private String likelihood;
    private int confirmations;

    @Override
    public String toString() {
        StringBuilder searchFilterString = new StringBuilder();
        if (!Strings.isNullOrEmpty(getEvent())) {
            searchFilterString.append("Event: ");
            searchFilterString.append(getEvent());
            searchFilterString.append(", ");
        }
        if (!Strings.isNullOrEmpty(getLocation())) {
            searchFilterString.append("Location: ");
            searchFilterString.append(getLocation());
            searchFilterString.append(", ");
        }
        if (!Strings.isNullOrEmpty(getCountry())) {
            searchFilterString.append("Country: ");
            searchFilterString.append(getCountry());
            searchFilterString.append(", ");
        }
        if (!Strings.isNullOrEmpty(getYear())) {
            searchFilterString.append("Year: ");
            searchFilterString.append(getYear());
            searchFilterString.append(", ");
        }
        if (!Strings.isNullOrEmpty(getLikelihood())) {
            searchFilterString.append("Likelihood: ");
            searchFilterString.append(getLikelihood());
            searchFilterString.append(", ");
        }
        if (getConfirmations() > 0) {
            searchFilterString.append("Confirmations: ");
            searchFilterString.append(getConfirmations());
        }

        if (searchFilterString.toString().endsWith(", ")) {
            return searchFilterString.substring(0, searchFilterString.length() - 2);
        }

        return searchFilterString.toString();
    }
}
