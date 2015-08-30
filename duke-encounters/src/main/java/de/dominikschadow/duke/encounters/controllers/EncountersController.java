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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class EncountersController {
    private EncounterService encounterService;
    private ValidationService validationService;

    @Autowired
    public EncountersController(EncounterService encounterService, ValidationService validationService) {
        this.encounterService = encounterService;
        this.validationService = validationService;
    }

    @RequestMapping(value = "/encounters", method = GET)
    public String allEncounters(Model model) {
        List<Encounter> encounters = encounterService.getAllEncounters();
        model.addAttribute("encounters", encounters);

        return "encounters";
    }

    @RequestMapping(value = "/encounters", method = POST)
    public String searchEncounters(@ModelAttribute(value="searchFilter") SearchFilter searchFilter, Model model) {
        validationService.validateSearchFilter(searchFilter);
        // TODO react on validation error
        List<Encounter> encounters = encounterService.getEncounters(searchFilter);
        model.addAttribute("encounters", encounters);

        return "encounters";
    }

    @RequestMapping(value = "/encounters/{id}", method = GET)
    public String encounterById(@PathVariable("id") long id, Model model) {

        return "user/encounterDetails";
    }
}
