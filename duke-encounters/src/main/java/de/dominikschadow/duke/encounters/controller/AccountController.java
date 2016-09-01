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

import de.dominikschadow.duke.encounters.domain.Confirmation;
import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.enums.Level;
import de.dominikschadow.duke.encounters.services.ConfirmationService;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.validators.DukeEncountersUserValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller to handle all account related requests.
 *
 * @author Dominik Schadow
 */
@Controller
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private EncounterService encounterService;
    private ConfirmationService confirmationService;
    private UserService userService;
    private DukeEncountersUserValidator validator;

    @Autowired
    public AccountController(final EncounterService encounterService, final ConfirmationService confirmationService,
                             final UserService userService, final DukeEncountersUserValidator validator) {
        this.encounterService = encounterService;
        this.confirmationService = confirmationService;
        this.userService = userService;
        this.validator = validator;
    }

    @RequestMapping(value = "/account", method = GET)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String showMyAccount(final Model model) {
        String username = userService.getUsername();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is accessing his account", username);

        List<Encounter> encounters = encounterService.getEncountersByUsername(username);
        model.addAttribute("encounters", encounters);

        List<Confirmation> confirmations = confirmationService.getConfirmationsByUsername(username);
        model.addAttribute("confirmations", confirmations);

        String userLevel = Level.ROOKIE.getName();

        DukeEncountersUser dukeEncountersUser = userService.getDukeEncountersUser(username);
        if (dukeEncountersUser != null && dukeEncountersUser.getLevel() != null) {
            userLevel = dukeEncountersUser.getLevel().getName();
        }

        model.addAttribute("userlevel", userLevel);

        return "user/account";
    }

    @RequestMapping(value = "/account/userdata", method = GET)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView editMyAccount() {
        String username = userService.getUsername();

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is editing his account", username);

        ModelAndView modelAndView = new ModelAndView("user/editAccount");

        DukeEncountersUser user = userService.getDukeEncountersUser();
        modelAndView.addObject("user", user);
        modelAndView.addObject("userlevel", user.getLevel().getName());

        return modelAndView;
    }

    /**
     * Updates the users first name and last name and stores it in the database.
     *
     * @param updatedUser        The updated user
     * @param redirectAttributes Attributes available after the redirect
     * @return Account page
     */
    @RequestMapping(value = "/account/userdata", method = POST)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView updateUser(@ModelAttribute final DukeEncountersUser updatedUser,
                                   final RedirectAttributes redirectAttributes) {
        DukeEncountersUser user = userService.getDukeEncountersUser();
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());

        DukeEncountersUser storedUser = userService.updateUser(user);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} updated", storedUser);

        redirectAttributes.addFlashAttribute("dataUpdated", true);

        return new ModelAndView("redirect:/account");
    }

    /**
     * Updates the users email and stores it in the database.
     *
     * @param updatedUser        The updated user
     * @param redirectAttributes Attributes available after the redirect
     * @return Account page
     */
    @RequestMapping(value = "/account/accountdata", method = POST)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView updateAccount(@ModelAttribute final DukeEncountersUser updatedUser,
                                      final RedirectAttributes redirectAttributes) {
        if (userService.confirmPassword(updatedUser.getPassword())) {
            DukeEncountersUser user = userService.getDukeEncountersUser();
            user.setEmail(updatedUser.getEmail());

            DukeEncountersUser storedUser = userService.updateUser(user);

            logger.warn(SecurityMarkers.SECURITY_AUDIT, "User {} updated", storedUser);

            redirectAttributes.addFlashAttribute("dataUpdated", true);
        } else {
            redirectAttributes.addFlashAttribute("dataNotUpdated", true);
        }

        return new ModelAndView("redirect:/account");
    }

    /**
     * Shows the registration page.
     *
     * @param dukeEncountersUser The new DukeEncountersUser
     * @return Register URL
     */
    @RequestMapping(value = "/register", method = GET)
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
    @RequestMapping(value = "/register", method = POST)
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
