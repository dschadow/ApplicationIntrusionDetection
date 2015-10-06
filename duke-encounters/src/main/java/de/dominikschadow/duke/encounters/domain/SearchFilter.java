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
package de.dominikschadow.duke.encounters.domain;

import com.google.common.base.Strings;

import java.time.LocalDateTime;

/**
 * SearchFilter to search for Duke encounters based on various fields.
 *
 * @author Dominik Schadow
 */
public class SearchFilter {
    private String event;
    private String location;
    private String country;
    private int year = LocalDateTime.now().getYear();
    private String likelihood;
    private int confirmations;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(String likelihood) {
        this.likelihood = likelihood;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

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
        if (getYear() > 0) {
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
