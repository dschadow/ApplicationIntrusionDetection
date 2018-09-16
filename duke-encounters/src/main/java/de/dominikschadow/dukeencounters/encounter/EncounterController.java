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
package de.dominikschadow.dukeencounters.encounter;

import de.dominikschadow.dukeencounters.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.owasp.appsensor.core.DetectionPoint.Category.REQUEST;

/**
 * Controller to handle all encounter related requests.
 *
 * @author Dominik Schadow
 */
@Controller
@AllArgsConstructor
public class EncounterController {
    private final EncounterService encounterService;
    private final EncounterValidator validator;
    private final UserService userService;
    private final DetectionSystem detectionSystem;
    private final EventManager ids;

    @GetMapping("/encounters")
    public String getEncounters(final Model model, final Principal principal, @RequestParam(name = "type", required = false) final String type) {
        boolean confirmable = !StringUtils.equals(principal.getName(), "anonymousUser") && !StringUtils.equals("own", type);

        List<Encounter> encounters = encounterService.getEncounters(type);
        model.addAttribute("encounters", encounters);
        model.addAttribute("confirmable", confirmable);

        return "encounters";
    }

    @GetMapping("/encounter/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String createEncounter(@ModelAttribute final Encounter encounter) {
        return "user/createEncounter";
    }

    @PostMapping("/encounter/create")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String saveEncounter(@Valid final Encounter encounter, final Model model, final BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("formErrors", result.getAllErrors());
            return "user/createEncounter";
        }

        encounterService.createEncounter(encounter);
        model.addAttribute("confirmable", true);

        return "redirect:/encounters";
    }

    @PostMapping("/encounter/delete")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ModelAndView deleteEncounter(final long encounterId) {
        encounterService.deleteEncounter(encounterId);

        return new ModelAndView("redirect:/account");
    }

    @GetMapping("/encounter/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String encounterById(@PathVariable("id") final long encounterId, final Model model,
                                final RedirectAttributes redirectAttributes) {
        Encounter encounter = encounterService.getEncounterById(encounterId);

        if (encounter == null) {
            fireInvalidUrlParameterEvent();
            redirectAttributes.addFlashAttribute("encounterFailure", true);
            redirectAttributes.addFlashAttribute("confirmable", true);

            return "redirect:/encounters";
        }

        model.addAttribute("encounter", encounter);

        return "user/encounterDetails";
    }

    private void fireInvalidUrlParameterEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(REQUEST, "RE8-001");
        ids.addEvent(new Event(userService.getAppSensorUser(), detectionPoint, detectionSystem));
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
