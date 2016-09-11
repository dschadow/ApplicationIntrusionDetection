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
package de.dominikschadow.dukeencounters.user;

import de.dominikschadow.dukeencounters.encounter.DukeEncountersUser;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Controller to handle all user related requests.
 *
 * @author Dominik Schadow
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final DukeEncountersUserValidator validator;

    public UserController(final UserService userService, final DukeEncountersUserValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * Shows the registration page.
     *
     * @param dukeEncountersUser The new DukeEncountersUser
     * @return Register URL
     */
    @GetMapping("/register")
    public String register(@ModelAttribute final DukeEncountersUser dukeEncountersUser) {
        return "register";
    }

    /**
     * Creates the new user and stores it in the database.
     *
     * @param dukeEncountersUser The new user to register
     * @param result The binding result
     * @return Login URL
     */
    @PostMapping("/register")
    public ModelAndView createUser(@Valid final DukeEncountersUser dukeEncountersUser, final BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("register", "formErrors", result.getAllErrors());
        }

        DukeEncountersUser newUser = userService.createUser(dukeEncountersUser);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} created", newUser);

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userCreated", newUser.getUsername());

        return modelAndView;
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
