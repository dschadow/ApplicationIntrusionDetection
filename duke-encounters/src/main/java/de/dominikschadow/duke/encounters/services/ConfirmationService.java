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

import de.dominikschadow.duke.encounters.domain.Confirmation;
import de.dominikschadow.duke.encounters.repositories.ConfirmationRepository;
import de.dominikschadow.duke.encounters.spring.Loggable;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * CRUD service for all confirmation related operations.
 *
 * @author Dominik Schadow
 */
@Service
public class ConfirmationService {
    @Loggable
    private Logger logger;

    private final ConfirmationRepository repository;
    private final UserService userService;
    private final EncounterService encounterService;

    @Autowired
    public ConfirmationService(ConfirmationRepository repository, UserService userService, EncounterService encounterService) {
        this.repository = repository;
        this.userService = userService;
        this.encounterService = encounterService;
    }

    public List<Confirmation> getConfirmationsByUsername(@NotNull String username) {
        List<Confirmation> confirmations = repository.findAllByUsername(username);

        logger.info("Query for user {} confirmations returned {} confirmations", username, confirmations.size());

        return confirmations;
    }

    public Confirmation getConfirmationByUsernameAndEncounterId(@NotNull String username, @NotNull long encounterId) {
        Confirmation confirmation = repository.findByUsernameAndEncounterId(username, encounterId);

        logger.info("Query for user {} confirmations returned {}", username, confirmation);

        return confirmation;
    }

    public void addConfirmation(@NotNull String username, @NotNull long encounterId) {
        Confirmation newConfirmation = new Confirmation();
        newConfirmation.setUser(userService.getDukeEncountersUser(username));
        newConfirmation.setDate(new Date());
        newConfirmation.setEncounter(encounterService.getEncounterById(encounterId));

        Confirmation confirmation = repository.save(newConfirmation);

        logger.info("Created new confirmation {}", confirmation);
    }

    public void deleteConfirmation(@NotNull String username, @NotNull long confirmationId) {
        repository.delete(confirmationId);

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} deleted confirmation {}", username, confirmationId);
    }

    public boolean hasConfirmedEncounter(@NotNull String username, @NotNull long encounterId) {
        return getConfirmationByUsernameAndEncounterId(username, encounterId) != null;
    }
}
