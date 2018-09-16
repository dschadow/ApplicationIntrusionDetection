/*
 * Copyright (C) 2018 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.dukeencounters.home;

import de.dominikschadow.dukeencounters.encounter.Encounter;
import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for the main page.
 *
 * @author Dominik Schadow
 */
@Controller
@AllArgsConstructor
public class HomeController {
    private final EncounterService encounterService;

    /**
     * Queries for the latest encounters, adds them to the model and returns the index page.
     *
     * @param model The model attribute container
     * @return Index URL
     */
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, final Model model) {
        List<Encounter> encounters = encounterService.getLatestEncounters(user.getUsername());
        model.addAttribute("encounters", encounters);

        return "index";
    }
}
