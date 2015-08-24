package de.dominikschadow.duke.encounters.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @RequestMapping(value = "login", method = GET)
    public String login() {
        return "login";
    }

    /**
     * Shows the login form with an error message.
     *
     * @param model The model attribute container
     * @return Login URL
     */
    @RequestMapping(value = "/login-error", method = GET)
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    /**
     * Shows the registration page.
     *
     * @return Registration URL
     */
    @RequestMapping(value = "register", method = GET)
    public String register() {
        return "register";
    }
}
