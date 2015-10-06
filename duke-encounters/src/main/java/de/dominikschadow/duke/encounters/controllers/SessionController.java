package de.dominikschadow.duke.encounters.controllers;

import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView("login");

        if (error != null) {
            model.addObject("loginError", true);
        }

        return model;
    }

    /**
     * Shows the registration page.
     *
     * @param model The model attribute container
     * @return Register URL
     */
    @RequestMapping(value = "/register", method = GET)
    public String register(Model model) {
        model.addAttribute("dukeEncountersUser", new DukeEncountersUser());
        return "register";
    }
}
