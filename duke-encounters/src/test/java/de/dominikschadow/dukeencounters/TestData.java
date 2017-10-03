/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import de.dominikschadow.dukeencounters.confirmation.Confirmation;
import de.dominikschadow.dukeencounters.encounter.DukeEncountersUser;
import de.dominikschadow.dukeencounters.encounter.Encounter;
import de.dominikschadow.dukeencounters.search.SearchFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Sets up different test data.
 *
 * @author Dominik Schadow
 */
public class TestData {
    public static Confirmation testConfirmation() {
        Confirmation testConfirmation = new Confirmation();
        testConfirmation.setId(1);
        testConfirmation.setEncounter(testEncounter(1));
        testConfirmation.setUser(testUser());

        return testConfirmation;
    }

    public static DukeEncountersUser testUser() {
        DukeEncountersUser testUser = new DukeEncountersUser();
        testUser.setUsername("test");

        return testUser;
    }

    public static Encounter testEncounter(long id) {
        Encounter testEncounter = new Encounter();
        testEncounter.setId(id);
        testEncounter.setComment("Test");
        testEncounter.setCountry("Test");
        testEncounter.setDate(new Date());

        return testEncounter;
    }

    public static List<Encounter> twoTestEncounters() {
        List<Encounter> encounters = new ArrayList<>();
        encounters.add(testEncounter(1));
        encounters.add(testEncounter(2));

        return encounters;
    }

    public static List<Encounter> threeTestEncounters() {
        List<Encounter> encounters = new ArrayList<>();
        encounters.add(testEncounter(1));
        encounters.add(testEncounter(2));
        encounters.add(testEncounter(3));

        return encounters;
    }

    public static SearchFilter searchFilter() {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setEvent("JavaOne");
        searchFilter.setLocation("San Francisco");
        searchFilter.setCountry("USA");
        searchFilter.setYear("2015");
        searchFilter.setConfirmations(1);
        searchFilter.setLikelihood("ANY");

        return searchFilter;
    }
}
