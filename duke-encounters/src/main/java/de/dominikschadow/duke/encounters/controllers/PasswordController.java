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
import de.dominikschadow.duke.encounters.domain.PasswordUpdate;
import de.dominikschadow.duke.encounters.services.ConfirmationService;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.validators.DukeEncountersUserValidator;
import de.dominikschadow.duke.encounters.validators.PasswordUpdateValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle a password change request.
 *
 * @author Dominik Schadow
 */
@Controller
public class PasswordController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordUpdateValidator passwordUpdateValidator;

    /**
     * Updates the users password and stores it in the database.
     *
     * @param update The new password
     * @return Account page
     */
    @RequestMapping(value = "/account/password/update", method = POST)
    public ModelAndView updatePassword(@ModelAttribute PasswordUpdate update, RedirectAttributes
            redirectAttributes) {
        LOGGER.warn("password {}", update.getCurrentPassword());
        LOGGER.warn("password {}", update.getNewPassword());
        LOGGER.warn("password {}", update.getNewPasswordConfirmation());
        if (userService.confirmPassword(update.getCurrentPassword())) {
            if (update.getNewPassword().equals(update.getNewPasswordConfirmation())) {
                DukeEncountersUser user = userService.getDukeEncountersUser();
                user.setPassword(userService.hashPassword(update.getNewPassword()));
                DukeEncountersUser storedUser = userService.updateUser(user);

                LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "Updated password of user {}", storedUser);
                redirectAttributes.addFlashAttribute("dataUpdated", "Password successfully updated.");
            } else {
                redirectAttributes.addFlashAttribute("dataNotUpdated", "The passwords do not match..");
            }
        } else {
            redirectAttributes.addFlashAttribute("dataNotUpdated", "Password update failed.");
        }

        return new ModelAndView("redirect:/account");
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(passwordUpdateValidator);
    }
}
