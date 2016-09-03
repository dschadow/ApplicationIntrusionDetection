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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Controller for all session related requests (login, logout, registration).
 *
 * @author Dominik Schadow
 */
@Controller
public class SessionController {
    /**
     * Shows the login page.
     *
     * @return Login URL
     */
    @RequestMapping(value = "/login", method = GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) final String error) {
        ModelAndView model = new ModelAndView("login");

        if (error != null) {
            model.addObject("loginError", true);
        }

        return model;
    }
}
