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

import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.domain.PasswordChange;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.spring.Loggable;
import de.dominikschadow.duke.encounters.validators.PasswordChangeValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle a password change request.
 *
 * @author Dominik Schadow
 */
@Controller
public class PasswordController {
    @Loggable
    private Logger logger;

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordChangeValidator passwordChangeValidator;

    /**
     * Loads the change password page.
     *
     * @param passwordChange The PasswordChange model attribute
     * @return The page to navigate to, including userlevel information
     */
    @RequestMapping(value = "/account/password/edit", method = GET)
    public ModelAndView changePassword(@ModelAttribute PasswordChange passwordChange) {
        String username = userService.getUsername();

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} is changing his password", username);

        ModelAndView modelAndView = new ModelAndView("user/changePassword");

        DukeEncountersUser user = userService.getDukeEncountersUser();
        modelAndView.addObject("userlevel", user.getLevel().getName());

        return modelAndView;
    }

    /**
     * Updates the users password and stores it in the database.
     *
     * @param update The new password
     * @return Account page
     */
    @RequestMapping(value = "/account/password/update", method = POST)
    public ModelAndView updatePassword(@ModelAttribute PasswordChange update, RedirectAttributes
            redirectAttributes, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("user/changePassword", "formErrors", result.getAllErrors());
        }

        DukeEncountersUser user = userService.getDukeEncountersUser();
        user.setPassword(userService.hashPassword(update.getNewPassword()));

        DukeEncountersUser storedUser = userService.updateUser(user);

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} changed his password", storedUser.getUsername());

        redirectAttributes.addFlashAttribute("dataUpdated", "Password successfully changed.");

        return new ModelAndView("redirect:/account");
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(passwordChangeValidator);
    }
}
