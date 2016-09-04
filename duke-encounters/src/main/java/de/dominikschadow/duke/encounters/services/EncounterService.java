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
package de.dominikschadow.duke.encounters.services;

import com.google.common.base.Strings;
import de.dominikschadow.duke.encounters.Constants;
import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.repositories.EncounterRepository;
import de.dominikschadow.duke.encounters.repositories.EncounterSpecification;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionSystem;
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
import java.util.Objects;

import static org.owasp.appsensor.core.DetectionPoint.Category.COMMAND_INJECTION;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * CRUD service for all encounter related operations.
 *
 * @author Dominik Schadow
 */
@Service
public class EncounterService {
    private static final Logger logger = LoggerFactory.getLogger(EncounterService.class);

    private final EncounterRepository repository;
    private final UserService userService;
    private final EventManager ids;
    private final DetectionSystem detectionSystem;
    private final int latestEncounterAmount;

    @Autowired
    public EncounterService(final EncounterRepository repository, final UserService userService,
                            final EventManager ids, final DetectionSystem detectionSystem,
                            @Value("${encounters.latest.amount}") final int latestEncounterAmount) {
        this.repository = repository;
        this.userService = userService;
        this.ids = ids;
        this.detectionSystem = detectionSystem;
        this.latestEncounterAmount = latestEncounterAmount;
    }

    public List<Encounter> getLatestEncounters() {
        Pageable latestEncounters = new PageRequest(0, latestEncounterAmount, Sort.Direction.DESC, "date");
        List<Encounter> encounters = repository.findWithPageable(latestEncounters);

        if (encounters.size() > latestEncounterAmount) {
            fireSqlIEvent();
        }

        return encounters;
    }

    public List<Encounter> getEncounters(@NotNull final SearchFilter filter) {
        List<Specification> specifications = new ArrayList<>();

        String event = Constants.LIKE;
        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            event += filter.getEvent() + Constants.LIKE;
        }
        specifications.add(EncounterSpecification.encounterByEvent(event));

        String location = Constants.LIKE;
        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            location += filter.getLocation() + Constants.LIKE;
        }
        specifications.add(EncounterSpecification.encounterByLocation(location));

        String country = Constants.LIKE;
        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            country += filter.getCountry() + Constants.LIKE;
        }
        specifications.add(EncounterSpecification.encounterByCountry(country));

        try {
            int year = Integer.parseInt(filter.getYear());

            if (year > Constants.YEAR_OF_JAVA_CREATION) {
                specifications.add(EncounterSpecification.encounterAfterYear(year));
            }
        } catch (NumberFormatException ex) {
            logger.info("{} is not a valid year", filter.getYear());
        }

        //specifications.add(EncounterSpecification.encounterByLikelihood(Likelihood.fromString(filter.getLikelihood
        // ())));

        //specifications.add(EncounterSpecification.encounterByConfirmations(filter.getConfirmations()));

        return repository.findAll(where(specifications.get(0)).and(specifications.get(1)).and(specifications.get(2)));
    }

    public Encounter getEncounterById(@NotNull final long encounterId) {
        String username = userService.getUsername();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "Querying details for encounter with id {}", encounterId);

        Encounter encounter = repository.findOne(encounterId);

        if (encounter == null) {
            logger.info(SecurityMarkers.SECURITY_FAILURE, "User {} tried to access encounter {} which does not exist",
                    username, encounterId);
        }

        return encounter;
    }

    public List<Encounter> getEncountersByUsername(@NotNull final String username) {
        List<Encounter> encounters = repository.findAllByUsername(username);

        logger.info("Query for user {} encounters returned {} encounters", username, encounters.size());

        return encounters;
    }

    public void deleteEncounter(@NotNull final long encounterId) {
        String username = userService.getUsername();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to delete encounter {}", username, encounterId);

        repository.delete(encounterId);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} deleted encounter {}", username, encounterId);
    }

    public Encounter createEncounter(@NotNull final Encounter newEncounter) {
        DukeEncountersUser user = userService.getDukeEncountersUser();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to create a new encounter {}",
                user.getUsername(), newEncounter);

        newEncounter.setUser(user);

        Encounter encounter = repository.save(newEncounter);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} created encounter {}", user.getUsername(), newEncounter);

        return encounter;
    }

    public boolean isOwnEncounter(@NotNull final long encounterId, @NotNull final String username) {
        Encounter encounter = repository.findByIdAndUsername(encounterId, username);

        boolean owner = encounter != null;

        logger.info("User {} is {} owner of encounter {}", username, owner ? "the" : "not the", encounterId);

        return owner;
    }

    public List<Encounter> getEncountersByEvent(final String event) {
        List<Encounter> encounters = repository.findByEventContaining(event);

        logger.info("Query for event {} returned {} encounters", event, encounters.size());

        return encounters;
    }

    public List<Encounter> getEncounters(final String type) {
        List<Encounter> encounters;

        if (Objects.equals("own", type)) {
            String username = userService.getUsername();

            logger.warn(SecurityMarkers.SECURITY_AUDIT, "Querying encounters for user {}", username);

            encounters = repository.findAllByUsername(username);
        } else {
            encounters = repository.findAll();
        }

        logger.info("Query returned {} encounters", encounters.size());

        return encounters;
    }

    private void fireSqlIEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(COMMAND_INJECTION, "CIE1-002");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }
}
