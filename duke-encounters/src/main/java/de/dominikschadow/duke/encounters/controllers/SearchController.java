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
import de.dominikschadow.duke.encounters.validators.SearchFilterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for all search related requests.
 *
 * @author Dominik Schadow
 */
@Controller
public class SearchController {
    private EncounterService encounterService;
    private SearchFilterValidator searchFilterValidator;

    @Autowired
    public SearchController(EncounterService encounterService, SearchFilterValidator searchFilterValidator) {
        this.encounterService = encounterService;
        this.searchFilterValidator = searchFilterValidator;
    }

    /**
     * Shows the search form.
     *
     * @param searchFilter The new SearchFilter
     * @return Search URL
     */
    @RequestMapping(value = "/search", method = GET)
    public String searchEncounters(@ModelAttribute SearchFilter searchFilter) {
        return "search";
    }

    @RequestMapping(value = "/encounters", method = POST)
    public ModelAndView searchEncounters(@Valid SearchFilter searchFilter, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/search", "formErrors", result.getAllErrors());
        }

        List<Encounter> encounters = encounterService.getEncounters(searchFilter);

        Map<String, Object> modelMap = new LinkedHashMap<>();
        modelMap.put("encounters", encounters);
        modelMap.put("searchFilter", searchFilter);

        return new ModelAndView("/encounters", modelMap);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(searchFilterValidator);
    }
}
