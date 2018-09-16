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
package de.dominikschadow.dukeencounters.registration;

import de.dominikschadow.dukeencounters.user.User;
import de.dominikschadow.dukeencounters.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.security.logging.SecurityMarkers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Controller to handle all registration related requests.
 *
 * @author Dominik Schadow
 */
@Controller
@Slf4j
@AllArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates the new user and stores it in the database.
     *
     * @param registrationForm The new user data to register
     * @param errors Errors in the page
     * @return Login URL
     */
    @PostMapping("/register")
    public String createUser(@Valid final RegistrationForm registrationForm, final Errors errors) {
        if (errors.hasErrors()) {
//            return new ModelAndView("register", "formErrors", errors.getAllErrors());
            return "/register";
        }

        User newUser = userService.createUser(registrationForm.toUser(passwordEncoder));

        log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} created", newUser);

//        ModelAndView modelAndView = new ModelAndView("login");
//        modelAndView.addObject("userCreated", newUser.getUsername());

        return "redirect:/login";
    }
}
