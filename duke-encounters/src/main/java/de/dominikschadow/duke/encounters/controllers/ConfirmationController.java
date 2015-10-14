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
package de.dominikschadow.duke.encounters.controllers;

import de.dominikschadow.duke.encounters.services.ConfirmationService;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle all encounter confirmation related requests.
 */
@Controller
public class ConfirmationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationController.class);

    private ConfirmationService confirmationService;
    private EncounterService encounterService;
    private UserService userService;

    @Autowired
    public ConfirmationController(ConfirmationService confirmationService, EncounterService encounterService,
                                  UserService userService) {
        this.confirmationService = confirmationService;
        this.encounterService = encounterService;
        this.userService = userService;
    }

    @RequestMapping(value = "/confirmations/add", method = POST)
    public ModelAndView addConfirmation(long encounterId) {
        String username = userService.getUsername();

        if (encounterService.isOwnEncounter(encounterId, username)) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "User {} is owner of encounter {} and tried to confirm it",
                    username, encounterId);

            // TODO AID react to confirming own encounter
            return new ModelAndView("redirect:/account");
        }

        if (confirmationService.hasConfirmedEncounter(username, encounterId)) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "User {} has already confirmed encounter {} and tried to " +
                            "confirm it again",
                    username, encounterId);
            // TODO AID react to double confirmations
            return new ModelAndView("redirect:/account");
        }

        confirmationService.addConfirmation(username, encounterId);

        LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "User {} confirmed encounter {}", username, encounterId);

        return new ModelAndView("redirect:/account");
    }

    @RequestMapping(value = "/confirmations/revoke", method = POST)
    public ModelAndView revokeConfirmation(long confirmationId) {
        String username = userService.getUsername();

        confirmationService.deleteConfirmation(username, confirmationId);

        LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "User {} revoked confirmation {}", username, confirmationId);

        return new ModelAndView("redirect:/account");
    }
}
