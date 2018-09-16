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
package de.dominikschadow.dukeencounters.account;

import de.dominikschadow.dukeencounters.confirmation.Confirmation;
import de.dominikschadow.dukeencounters.confirmation.ConfirmationService;
import de.dominikschadow.dukeencounters.encounter.Encounter;
import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.encounter.User;
import de.dominikschadow.dukeencounters.user.DukeEncountersUserValidator;
import de.dominikschadow.dukeencounters.user.Level;
import de.dominikschadow.dukeencounters.user.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.security.logging.SecurityMarkers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller to handle all account related requests.
 *
 * @author Dominik Schadow
 */
@Controller
@Slf4j
@AllArgsConstructor
public class AccountController {
    private final EncounterService encounterService;
    private final ConfirmationService confirmationService;
    private final UserService userService;
    private final DukeEncountersUserValidator validator;

    @GetMapping("/account")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String showMyAccount(final Model model) {
        String username = userService.getUsername();

        log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is accessing his account", username);

        List<Encounter> encounters = encounterService.getEncountersByUsername(username);
        model.addAttribute("encounters", encounters);

        List<Confirmation> confirmations = confirmationService.getConfirmationsByUsername(username);
        model.addAttribute("confirmations", confirmations);

        String userLevel = Level.ROOKIE.getName();

        User dukeEncountersUser = userService.getDukeEncountersUser(username);
        if (dukeEncountersUser != null && dukeEncountersUser.getLevel() != null) {
            userLevel = dukeEncountersUser.getLevel().getName();
        }

        model.addAttribute("userlevel", userLevel);

        return "user/account";
    }

    @GetMapping("/account/userdata")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView editMyAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} is editing his account", user.getUsername());

        ModelAndView modelAndView = new ModelAndView("user/editAccount");
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
    @PostMapping("/account/userdata")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView updateUser(Authentication authentication, @ModelAttribute final User updatedUser,
                                   final RedirectAttributes redirectAttributes) {
        User user = (User) authentication.getPrincipal();

        User storedUser = userService.updateUser(user, updatedUser);

        log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} updated his user data", storedUser);

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
    @PostMapping("/account/accountdata")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView updateAccount(Authentication authentication, @ModelAttribute final User updatedUser,
                                      final RedirectAttributes redirectAttributes) {
        User user = (User) authentication.getPrincipal();

        if (userService.confirmPassword(updatedUser.getPassword())) {
            User storedUser = userService.updateUser(user, updatedUser);

            log.warn(SecurityMarkers.SECURITY_AUDIT, "User {} updated his user data", storedUser);

            redirectAttributes.addFlashAttribute("dataUpdated", true);
        } else {
            redirectAttributes.addFlashAttribute("dataNotUpdated", true);
        }

        return new ModelAndView("redirect:/account");
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
