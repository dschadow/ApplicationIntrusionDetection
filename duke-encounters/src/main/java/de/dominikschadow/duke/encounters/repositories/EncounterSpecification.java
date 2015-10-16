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
package de.dominikschadow.duke.encounters.repositories;

import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.Likelihood;
import org.springframework.data.jpa.domain.Specification;

import java.util.Calendar;
import java.util.Date;

/**
 * Specification to filter encounters based on the entered search filter.
 *
 * @author Dominik Schadow
 */
public class EncounterSpecification {
    public static Specification<Encounter> encounterAfterYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Date>get("date"), calendar.getTime());
    }

    public static Specification<Encounter> encounterByConfirmations(int confirmations) {
        // TODO count confirmations
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<Integer>get(""), confirmations);
    }

    public static Specification<Encounter> encounterByEvent(String event) {
        return (root, query, cb) -> cb.like(root.<String>get("event"), event);
    }

    public static Specification<Encounter> encounterByLocation(String location) {
        return (root, query, cb) -> cb.like(root.<String>get("location"), location);
    }

    public static Specification<Encounter> encounterByCountry(String country) {
        return (root, query, cb) -> cb.like(root.<String>get("country"), country);
    }

    public static Specification<Encounter> encounterByLikelihood(Likelihood likelihood) {
        return (root, query, cb) -> cb.equal(root.<String>get("likelihood"), likelihood.getName());
    }
}
