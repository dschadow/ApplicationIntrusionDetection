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
import de.dominikschadow.duke.encounters.validators.EncounterValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle all encounter related requests.
 */
@Controller
public class EncountersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncountersController.class);

    private EncounterService encounterService;
    private EncounterValidator encounterValidator;
    private UserService userService;

    @Autowired
    public EncountersController(EncounterService encounterService, EncounterValidator encounterValidator, UserService
            userService) {
        this.encounterService = encounterService;
        this.encounterValidator = encounterValidator;
        this.userService = userService;
    }

    @RequestMapping(value = "/encounters", method = GET)
    public String allEncounters(Model model) {
        List<Encounter> encounters = encounterService.getAllEncounters();
        model.addAttribute("encounters", encounters);

        return "/encounters";
    }

    @RequestMapping(value = "/encounter/create", method = GET)
    public String createEncounter(@ModelAttribute Encounter encounter) {
        return "user/createEncounter";
    }

    @RequestMapping(value = "/encounter/create", method = POST)
    public ModelAndView saveEncounter(@Valid Encounter newEncounter, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/encounter/create", "formErrors", result.getAllErrors());
        }

        String username = userService.getUsername();

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to create a new encounter {}", username,
                newEncounter);

        Encounter encounter = encounterService.createEncounter(newEncounter, username);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} successfully created encounter {}", username, encounter);

        return new ModelAndView("redirect:/account");
    }

    @RequestMapping(value = "/encounter/delete", method = POST)
    public ModelAndView deleteEncounter(long encounterId) {
        String username = userService.getUsername();

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} is trying to delete encounter {}", username, encounterId);

        // TODO AID react to not own encounter

        encounterService.deleteEncounter(username, encounterId);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} successfully deleted encounter {}", username, encounterId);

        return new ModelAndView("redirect:/account");
    }

    @RequestMapping(value = "/encounters/{id}", method = GET)
    public String encounterById(@PathVariable("id") long id, Model model) {
        // TODO react on validation error

        Encounter encounter = encounterService.getEncounterById(id);
        model.addAttribute("encounter", encounter);

        return "/user/encounterDetails";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(encounterValidator);
    }
}
