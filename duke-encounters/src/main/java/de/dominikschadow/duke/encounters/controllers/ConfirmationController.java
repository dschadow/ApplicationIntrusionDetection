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

import de.dominikschadow.duke.encounters.appsensor.IntrusionDetectionService;
import de.dominikschadow.duke.encounters.services.ConfirmationService;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle all encounter confirmation related requests.
 *
 * @author Dominik Schadow
 */
@Controller
public class ConfirmationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationController.class);

    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private EncounterService encounterService;
    @Autowired
    private UserService userService;
    @Autowired
    private IntrusionDetectionService intrusionDetectionService;
    @Autowired
    private EventManager ids;

    @RequestMapping(value = "/confirmations/add", method = POST)
    public ModelAndView addConfirmation(long encounterId, RedirectAttributes redirectAttributes) {
        String username = userService.getUsername();

        ModelAndView modelAndView = new ModelAndView("redirect:/account");

        if (encounterService.isOwnEncounter(encounterId, username)) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "User {} is owner of encounter {} and tried to confirm it",
                    username, encounterId);

            fireConfirmationErrorEvent();
            redirectAttributes.addFlashAttribute("confirmationFailure", "This is your own encounter and cannot be " +
                    "confirmed by yourself.");

            return modelAndView;
        }

        if (confirmationService.hasConfirmedEncounter(username, encounterId)) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "User {} has already confirmed encounter {} and tried to " +
                    "confirm it again", username, encounterId);

            fireConfirmationErrorEvent();
            redirectAttributes.addFlashAttribute("confirmationFailure", "You have already confirmed this encounter " +
                    "and cannot confirm it again.");

            return modelAndView;
        }

        confirmationService.addConfirmation(username, encounterId);

        LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "User {} confirmed encounter {}", username, encounterId);

        return modelAndView;
    }

    @RequestMapping(value = "/confirmations/revoke", method = POST)
    public ModelAndView revokeConfirmation(long confirmationId) {
        String username = userService.getUsername();

        confirmationService.deleteConfirmation(username, confirmationId);

        LOGGER.info(SecurityMarkers.SECURITY_SUCCESS, "User {} revoked confirmation {}", username, confirmationId);

        return new ModelAndView("redirect:/account");
    }

    private void fireConfirmationErrorEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.INPUT_VALIDATION, "IE5-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, intrusionDetectionService.getDetectionSystem()));
    }
}
