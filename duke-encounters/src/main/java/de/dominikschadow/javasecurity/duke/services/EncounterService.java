/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
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
package de.dominikschadow.javasecurity.duke.services;

import de.dominikschadow.javasecurity.duke.domain.Encounter;
import de.dominikschadow.javasecurity.duke.repositories.EncounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

        // AID: max list size 10
        return encounters;
    }

    public List<Encounter> getAllEncounters() {
        List<Encounter> encounters = repository.findAll();

        return encounters;
    }
}
