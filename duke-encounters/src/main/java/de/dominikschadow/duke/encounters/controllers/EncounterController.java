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

import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.spring.Loggable;
import de.dominikschadow.duke.encounters.validators.EncounterValidator;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.owasp.appsensor.core.DetectionPoint.Category.REQUEST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle all encounter related requests.
 *
 * @author Dominik Schadow
 */
@Controller
public class EncounterController {
    @Loggable
    private Logger logger;

    private EncounterService encounterService;
    private EncounterValidator validator;
    private UserService userService;
    private DetectionSystem detectionSystem;
    private EventManager ids;

    @Autowired
    public EncounterController(EncounterService encounterService, EncounterValidator validator, UserService userService, DetectionSystem detectionSystem, EventManager ids) {
        this.encounterService = encounterService;
        this.validator = validator;
        this.userService = userService;
        this.detectionSystem = detectionSystem;
        this.ids = ids;
    }

    @RequestMapping(value = "/encounter", method = GET)
    public String getEncounters(Model model, @RequestParam(name = "type", required = false) String type) {
        List<Encounter> encounters = encounterService.getEncounters(type);
        model.addAttribute("encounters", encounters);

        return "encounters";
    }

    @RequestMapping(value = "/encounter/create", method = GET)
    public String createEncounter(@ModelAttribute Encounter encounter) {
        return "user/createEncounter";
    }

    @RequestMapping(value = "/encounter/create", method = POST)
    public ModelAndView saveEncounter(@Valid Encounter encounter, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("user/createEncounter", "formErrors", result.getAllErrors());
        }

        String username = userService.getUsername();

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to create a new encounter {}", username,
                encounter);

        Encounter newEncounter = encounterService.createEncounter(encounter, username);

        logger.info(SecurityMarkers.SECURITY_SUCCESS, "User {} created encounter {}", username,
                newEncounter);

        return new ModelAndView("redirect:/encounter");
    }

    @RequestMapping(value = "/encounter/delete", method = POST)
    public ModelAndView deleteEncounter(long encounterId) {
        String username = userService.getUsername();

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to delete encounter {}", username, encounterId);

        encounterService.deleteEncounter(username, encounterId);

        logger.info(SecurityMarkers.SECURITY_SUCCESS, "User {} deleted encounter {}", username, encounterId);

        return new ModelAndView("redirect:/account");
    }

    @RequestMapping(value = "/encounter/{id}", method = GET)
    public String encounterById(@PathVariable("id") long encounterId, Model model, RedirectAttributes
            redirectAttributes) {
        String username = userService.getUsername();

        if (encounterId < 1) {
            logger.info(SecurityMarkers.SECURITY_FAILURE, "User {} tried to access encounter with negative id {}",
                    username, encounterId);

            fireInvalidUrlParameterEvent();
            redirectAttributes.addFlashAttribute("encounterFailure", true);

            return "redirect:/encounter";
        }

        Encounter encounter = encounterService.getEncounterById(encounterId);

        if (encounter == null) {
            logger.info(SecurityMarkers.SECURITY_FAILURE, "User {} tried to access encounter {} which does not exist",
                    username, encounterId);

            fireInvalidUrlParameterEvent();
            redirectAttributes.addFlashAttribute("encounterFailure", true);

            return "redirect:/encounter";
        }

        model.addAttribute("encounter", encounter);

        return "user/encounterDetails";
    }

    private void fireInvalidUrlParameterEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(REQUEST, "RE8-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
