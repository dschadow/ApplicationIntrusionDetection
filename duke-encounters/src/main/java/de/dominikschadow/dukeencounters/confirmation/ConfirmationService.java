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
package de.dominikschadow.dukeencounters.confirmation;

import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.user.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.owasp.security.logging.SecurityMarkers;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * CRUD service for all confirmation related operations.
 *
 * @author Dominik Schadow
 */
@Service
@Slf4j
@AllArgsConstructor
public class ConfirmationService {
    private final ConfirmationRepository repository;
    private final UserService userService;
    private final EncounterService encounterService;

    public List<Confirmation> getConfirmationsByUsername(@NonNull final String username) {
        List<Confirmation> confirmations = repository.findAllByUsername(username);

        log.info("Query for user {} confirmations returned {} confirmations", username, confirmations.size());

        return confirmations;
    }

    public Confirmation getConfirmationByUsernameAndEncounterId(@NonNull final String username,
                                                                final long encounterId) {
        Confirmation confirmation = repository.findByUsernameAndEncounterId(username, encounterId);

        log.info("Query for user {} confirmations returned {}", username, confirmation);

        return confirmation;
    }

    public Confirmation addConfirmation(@NotNull final String username, final long encounterId) {
        Confirmation newConfirmation = new Confirmation();
        newConfirmation.setUser(userService.getDukeEncountersUser(username));
        newConfirmation.setDate(new Date());
        newConfirmation.setEncounter(encounterService.getEncounterById(encounterId));

        Confirmation confirmation = repository.save(newConfirmation);

        log.info("User {} created new {}", username, confirmation);

        return confirmation;
    }

    public void deleteConfirmation(@NonNull final String username, final long confirmationId) {
        repository.delete(confirmationId);

        log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} deleted confirmation {}", username, confirmationId);
    }

    public boolean hasConfirmedEncounter(@NonNull final String username, final long encounterId) {
        return getConfirmationByUsernameAndEncounterId(username, encounterId) != null;
    }

    public List<Confirmation> getConfirmations(final String type) {
        List<Confirmation> confirmations;

        if (Objects.equals("own", type)) {
            String username = userService.getUsername();

            log.warn(SecurityMarkers.SECURITY_AUDIT, "Querying confirmations for user {}", username);

            confirmations = repository.findAllByUsername(username);
        } else {
            confirmations = repository.findAll();
        }

        log.info("Query returned {} confirmations", confirmations.size());

        return confirmations;
    }
}
