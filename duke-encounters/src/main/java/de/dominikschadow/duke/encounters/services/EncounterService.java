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
package de.dominikschadow.duke.encounters.services;

import com.google.common.base.Strings;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.Likelihood;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.repositories.EncounterRepository;
import de.dominikschadow.duke.encounters.repositories.EncounterSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

@Service
public class EncounterService {
    private EncounterRepository repository;

    @Autowired
    public EncounterService(EncounterRepository repository) {
        this.repository = repository;
    }

    public List<Encounter> getLatestEncounters() {
        Pageable latestTen = new PageRequest(0, 10, Sort.Direction.DESC, "date");
        List<Encounter> encounters = repository.findWithPageable(latestTen);

        // TODO AID max list size 10
        return encounters;
    }

    public List<Encounter> getAllEncounters() {
        List<Encounter> encounters = repository.findAll();

        return encounters;
    }

    public List<Encounter> getEncounters(SearchFilter filter) {
        List<Specification> specifications = new ArrayList<>();

        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            specifications.add(EncounterSpecification.encounterByEvent(filter.getEvent()));
        }

        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            specifications.add(EncounterSpecification.encounterByLocation(filter.getLocation()));
        }

        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            specifications.add(EncounterSpecification.encounterByCountry(filter.getCountry()));
        }

        if (filter.getYear() > 0) {
            specifications.add(EncounterSpecification.encounterAfterYear(filter.getYear()));
        }

        if (!Strings.isNullOrEmpty(filter.getLikelihood())) {
            Likelihood likelihood = Likelihood.valueOf(filter.getLikelihood());
            specifications.add(EncounterSpecification.encounterByLikelihood(likelihood));
        }

        specifications.add(EncounterSpecification.encounterByConfirmations(filter.getConfirmations()));

        List<Encounter> encounters = repository.findAll(where(EncounterSpecification.encounterAfterYear(0)).and
                (EncounterSpecification.encounterByConfirmations(1)));

        return encounters;
    }
}
