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
package de.dominikschadow.dukeencounters.account;

import de.dominikschadow.dukeencounters.encounter.DukeEncountersUser;
import de.dominikschadow.dukeencounters.user.UserService;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller to handle a password change request.
 *
 * @author Dominik Schadow
 */
@Controller
public class PasswordController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    private UserService userService;
    private PasswordChangeValidator validator;

    public PasswordController(final UserService userService, final PasswordChangeValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    /**
     * Loads the change password page.
     *
     * @param passwordChange The PasswordChange model attribute
     * @return The page to navigate to, including user level information
     */
    @GetMapping("/account/password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView changePassword(@ModelAttribute final PasswordChange passwordChange) {
        String username = userService.getUsername();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is changing his password", username);

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
    @PostMapping("/account/password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView updatePassword(@Valid final PasswordChange update, final BindingResult result,
                                       final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return new ModelAndView("user/changePassword", "formErrors", result.getAllErrors());
        }

        DukeEncountersUser user = userService.getDukeEncountersUser();
        user.setPassword(userService.hashPassword(update.getNewPassword()));

        DukeEncountersUser storedUser = userService.updateUser(user);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} changed his password", storedUser.getUsername());

        redirectAttributes.addFlashAttribute("dataUpdated", true);

        return new ModelAndView("redirect:/account");
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
