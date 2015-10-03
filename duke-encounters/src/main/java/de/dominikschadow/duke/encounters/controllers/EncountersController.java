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
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.ValidationService;
import de.dominikschadow.duke.encounters.validators.SearchFilterValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
    private ValidationService validationService;
    private SearchFilterValidator searchFilterValidator;

    @Autowired
    public EncountersController(EncounterService encounterService, ValidationService validationService,
                                SearchFilterValidator searchFilterValidator) {
        this.encounterService = encounterService;
        this.validationService = validationService;
        this.searchFilterValidator = searchFilterValidator;
    }

    @RequestMapping(value = "/encounters", method = GET)
    public String allEncounters(Model model) {
        List<Encounter> encounters = encounterService.getAllEncounters();
        model.addAttribute("encounters", encounters);

        return "encounters";
    }

    @RequestMapping(value = "/encounters", method = POST)
    public String searchEncounters(@ModelAttribute(value = "searchFilter") SearchFilter searchFilter, Model model,
                                   BindingResult result) {
        //validationService.validateSearchFilter(searchFilter);
        // TODO react on validation error
        if (result.hasErrors()) {
            return "encounters";
        }
        List<Encounter> encounters = encounterService.getEncounters(searchFilter);
        model.addAttribute("encounters", encounters);

        return "encounters";
    }

    @RequestMapping(value = "/encounters/new", method = GET)
    public String newEncounter(Model model) {
        model.addAttribute("encounter", new Encounter());

        return "user/newEncounter";
    }

    @RequestMapping(value = "/encounters/delete", method = POST)
    public String deleteEncounter(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} deleted encounter {}", username, "");

        List<Encounter> encounters = encounterService.getEncountersByUsername(username);
        model.addAttribute("encounters", encounters);

        return "user/account";
    }

    @RequestMapping(value = "/encounters/confirm", method = POST)
    public String confirmEncounter(Model model) {

        // TODO AID react to double confirmations

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} confirmed encounter {}", username, "");

        List<Encounter> encounters = encounterService.getEncountersByUsername(username);
        model.addAttribute("encounters", encounters);

        return "user/account";
    }

    @RequestMapping(value = "/encounters/{id}", method = GET)
    public String encounterById(@PathVariable("id") long id, Model model) {
        validationService.validateEncounterId(id);
        // TODO react on validation error

        Encounter encounter = encounterService.getEncounterById(id);
        model.addAttribute("encounter", encounter);

        return "user/encounterDetails";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(searchFilterValidator);
    }
}
