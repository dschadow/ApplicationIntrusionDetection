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

import de.dominikschadow.duke.encounters.Loggable;
import de.dominikschadow.duke.encounters.domain.Confirmation;
import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.services.ConfirmationService;
import de.dominikschadow.duke.encounters.services.EncounterService;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.validators.DukeEncountersUserValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Loggable
    private Logger logger;

    @Autowired
    private EncounterService encounterService;
    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private UserService userService;
    @Autowired
    private DukeEncountersUserValidator dukeEncountersUserValidator;

    @RequestMapping(value = "/account", method = GET)
    public String showMyAccount(Model model) {
        String username = userService.getUsername();

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} is accessing his account", username);

        List<Encounter> encounters = encounterService.getEncountersByUsername(username);
        model.addAttribute("encounters", encounters);

        List<Confirmation> confirmations = confirmationService.getConfirmationsByUsername(username);
        model.addAttribute("confirmations", confirmations);

        DukeEncountersUser dukeEncountersUser = userService.getDukeEncountersUser(username);
        model.addAttribute("userlevel", dukeEncountersUser.getLevel().getName());

        return "/user/account";
    }

    @RequestMapping(value = "/account/edit", method = GET)
    public ModelAndView editMyAccount() {
        String username = userService.getUsername();

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} is editing his account", username);

        ModelAndView modelAndView = new ModelAndView("/user/editAccount");

        DukeEncountersUser user = userService.getDukeEncountersUser();
        modelAndView.addObject("user", user);
        modelAndView.addObject("userlevel", user.getLevel().getName());

        return modelAndView;
    }

    /**
     * Updates the users firstname and lastname and stores it in the database.
     *
     * @param updatedUser The updated user
     * @return Account page
     */
    @RequestMapping(value = "/account/userdata/update", method = POST)
    public ModelAndView updateUser(@ModelAttribute DukeEncountersUser updatedUser, RedirectAttributes
            redirectAttributes) {
        DukeEncountersUser user = userService.getDukeEncountersUser();
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());

        DukeEncountersUser storedUser = userService.updateUser(user);

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} updated", storedUser);

        redirectAttributes.addFlashAttribute("dataUpdated", "User data successfully updated.");

        return new ModelAndView("redirect:/account");
    }

    /**
     * Updates the users email and stores it in the database.
     *
     * @param updatedUser The updated user
     * @return Account page
     */
    @RequestMapping(value = "/account/accountdata/update", method = POST)
    public ModelAndView updateAccount(@ModelAttribute DukeEncountersUser updatedUser, RedirectAttributes
            redirectAttributes) {
        if (userService.confirmPassword(updatedUser.getPassword())) {
            DukeEncountersUser user = userService.getDukeEncountersUser();
            user.setEmail(updatedUser.getEmail());

            DukeEncountersUser storedUser = userService.updateUser(user);

            logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} updated", storedUser);

            redirectAttributes.addFlashAttribute("dataUpdated", "Account data successfully updated.");
        } else {
            redirectAttributes.addFlashAttribute("dataNotUpdated", "Failed to update account data.");
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
    public String register(@ModelAttribute DukeEncountersUser dukeEncountersUser) {
        return "/register";
    }

    /**
     * Creates the new user and stores it in the database.
     *
     * @param dukeEncountersUser The new user to register
     * @return Login URL
     */
    @RequestMapping(value = "/register", method = POST)
    public ModelAndView createUser(@Valid DukeEncountersUser dukeEncountersUser, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/register", "formErrors", result.getAllErrors());
        }

        DukeEncountersUser newUser = userService.createUser(dukeEncountersUser);

        logger.info(SecurityMarkers.SECURITY_AUDIT, "User {} created", newUser);

        ModelAndView modelAndView = new ModelAndView("/login");
        modelAndView.addObject("userCreated", "User " + newUser.getUsername() + " successfully created, you can now " +
                "use your new user to log in.");

        return modelAndView;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(dukeEncountersUserValidator);
    }
}
