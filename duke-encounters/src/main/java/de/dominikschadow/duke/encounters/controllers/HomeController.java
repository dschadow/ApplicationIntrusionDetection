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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HomeController {
    private EncounterService encounterService;

    @Autowired
    public HomeController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @RequestMapping(value = "/", method= GET)
    public String home(Model model) {
        List<Encounter> encounters = encounterService.getLatestEncounters();
        model.addAttribute("encounters", encounters);

        return "index";
    }

    @RequestMapping(value = "search", method = GET)
    public String searchEncounters(Model model) {
        model.addAttribute("searchFilter", new SearchFilter());

        return "search";
    }

    @RequestMapping(value = "login", method = GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "register", method = GET)
    public String register(Model model) {
        return "register";
    }
}
