package de.dominikschadow.duke.encounters.controllers;

import de.dominikschadow.duke.encounters.domain.User;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for all session related requests (login, logout, registration).
 *
 * @author Dominik Schadow
 */
@Controller
public class SessionController {
    private ValidationService validationService;
    private UserService userService;

    @Autowired
    public SessionController(ValidationService validationService, UserService userService) {
        this.validationService = validationService;
        this.userService = userService;
    }

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
     * @param model The model attribute container
     * @return Register URL
     */
    @RequestMapping(value = "/register", method = GET)
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Creates the new user and stored it in the database.
     *
     * @param model The model attribute container
     * @return Login URL
     */
    @RequestMapping(value = "/register", method = POST)
    public String register(@ModelAttribute(value = "user") User register, Model model) {
        validationService.validateUser(register);
        // TODO react on validation error
        User user = userService.createUser(register);
        model.addAttribute("user", user);

        return "login";
    }
}
