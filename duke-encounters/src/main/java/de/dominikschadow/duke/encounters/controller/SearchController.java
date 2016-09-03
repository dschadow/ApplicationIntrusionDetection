/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.duke.encounters.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
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
    private SearchFilterValidator validator;

    @Autowired
    public SearchController(final EncounterService encounterService, final SearchFilterValidator validator) {
        this.encounterService = encounterService;
        this.validator = validator;
    }

    /**
     * Shows the search form.
     *
     * @param searchFilter The new SearchFilter
     * @return Search URL
     */
    @RequestMapping(value = "/search", method = GET)
    public String searchEncounters(@ModelAttribute final SearchFilter searchFilter) {
        return "search";
    }

    /**
     * Uses the input text to search for the encounter event.
     *
     * @param event The events name
     * @return ModelAndView with encounters URL and a model map
     */
    @RequestMapping(value = "/search", method = POST)
    public ModelAndView searchEncounterByEvent(@RequestParam("quickSearch") final String event) {
        List<Encounter> encounters = encounterService.getEncountersByEvent(event);

        Map<String, Object> modelMap = new LinkedHashMap<>();
        modelMap.put("encounters", encounters);

        return new ModelAndView("encounters", modelMap);
    }

    /**
     * Search the encounters based on the given search filter.
     *
     * @param searchFilter The search filter identifying encounters
     * @param result       BindingResult
     * @return ModelAndView with encounters URL and a model map
     */
    @RequestMapping(value = "/encounters", method = POST)
    public ModelAndView searchEncounters(@Valid final SearchFilter searchFilter, final BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("search", "formErrors", result.getAllErrors());
        }

        List<Encounter> encounters = encounterService.getEncounters(searchFilter);

        Map<String, Object> modelMap = new LinkedHashMap<>();
        modelMap.put("encounters", encounters);
        modelMap.put("searchFilter", searchFilter);

        return new ModelAndView("encounters", modelMap);
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
