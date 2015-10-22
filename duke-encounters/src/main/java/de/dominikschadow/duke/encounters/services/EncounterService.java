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
import de.dominikschadow.duke.encounters.appsensor.IntrusionDetectionService;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.repositories.EncounterRepository;
import de.dominikschadow.duke.encounters.repositories.EncounterSpecification;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * CRUD service for all encounter related operations.
 *
 * @author Dominik Schadow
 */
@Service
public class EncounterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterService.class);

    private EncounterRepository encounterRepository;
    private UserService userService;
    @Autowired
    private EventManager ids;
    @Autowired
    private IntrusionDetectionService intrusionDetectionService;
    private static final String LIKE = "%";
    private int latestEncounterAmount;

    @Autowired
    public EncounterService(EncounterRepository encounterRepository, UserService userService,
                            @Value("${encounters.latest.amount}") int latestEncounterAmount) {
        this.encounterRepository = encounterRepository;
        this.userService = userService;
        this.latestEncounterAmount = latestEncounterAmount;
    }

    public List<Encounter> getLatestEncounters() {
        Pageable latestTen = new PageRequest(0, latestEncounterAmount, Sort.Direction.DESC, "date");
        List<Encounter> encounters = encounterRepository.findWithPageable(latestTen);

        if (encounters.size() > latestEncounterAmount) {
            fireSqlIEvent();
        }

        return encounters;
    }

    public List<Encounter> getAllEncounters() {
        List<Encounter> encounters = encounterRepository.findAll();

        LOGGER.info("Query for all encounters returned {} encounters", encounters.size());

        return encounters;
    }

    public List<Encounter> getEncounters(@NotNull SearchFilter filter) {
        List<Specification> specifications = new ArrayList<>();

        String event = LIKE;
        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            event = LIKE + filter.getEvent() + LIKE;
        }
        specifications.add(EncounterSpecification.encounterByEvent(event));

        String location = LIKE;
        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            location = LIKE + filter.getLocation() + LIKE;
        }
        specifications.add(EncounterSpecification.encounterByLocation(location));

        String country = LIKE;
        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            country = LIKE + filter.getCountry() + LIKE;
        }
        specifications.add(EncounterSpecification.encounterByCountry(country));

        int year = 1995;
        if (filter.getYear() > 0) {
            year = filter.getYear();
        }
        specifications.add(EncounterSpecification.encounterAfterYear(year));

        //specifications.add(EncounterSpecification.encounterByLikelihood(Likelihood.fromString(filter.getLikelihood
        // ())));

        //specifications.add(EncounterSpecification.encounterByConfirmations(filter.getConfirmations()));

        List<Encounter> encounters = encounterRepository.findAll(where(specifications.get(0)).and(specifications.get
                (1)).and(specifications.get(2)).and(specifications.get(3)));

        return encounters;
    }

    public Encounter getEncounterById(@NotNull long id) {
        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "Querying details for encounter with id {}", id);

        return encounterRepository.findOne(id);
    }

    public List<Encounter> getEncountersByUsername(@NotNull String username) {
        List<Encounter> encounters = encounterRepository.findAllByUsername(username);

        LOGGER.info("Query for user {} encounters returned {} encounters", username, encounters.size());

        return encounters;
    }

    public void deleteEncounter(@NotNull String username, @NotNull long encounterId) {
        encounterRepository.delete(encounterId);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} deleted encounter {}", username, encounterId);
    }

    public Encounter createEncounter(@NotNull Encounter newEncounter, @NotNull String username) {
        newEncounter.setUser(userService.getDukeEncountersUser());

        Encounter encounter = encounterRepository.save(newEncounter);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} created encounter {}", username, newEncounter);

        return encounter;
    }

    public boolean isOwnEncounter(@NotNull long encounterId, @NotNull String username) {
        Encounter encounter = encounterRepository.findByIdAndUsername(encounterId, username);

        boolean owner = encounter != null;

        LOGGER.info("User {} is {} owner of encounter {}", username, owner ? "the" : "not the", encounterId);

        return owner;
    }

    private void fireSqlIEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.COMMAND_INJECTION, "CIE1-002");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, intrusionDetectionService.getDetectionSystem()));
    }
}
