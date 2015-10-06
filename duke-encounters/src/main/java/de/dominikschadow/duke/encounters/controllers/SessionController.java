package de.dominikschadow.duke.encounters.controllers;

import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.services.UserService;
import de.dominikschadow.duke.encounters.validators.DukeEncountersUserValidator;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for all session related requests (login, logout, registration).
 *
 * @author Dominik Schadow
 */
@Controller
public class SessionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

    private UserService userService;
    private DukeEncountersUserValidator dukeEncountersUserValidator;

    @Autowired
    public SessionController(UserService userService, DukeEncountersUserValidator dukeEncountersUserValidator) {
        this.userService = userService;
        this.dukeEncountersUserValidator = dukeEncountersUserValidator;
    }

    /**
     * Shows the login page.
     *
     * @return Login URL
     */
    @RequestMapping(value = "/login", method = GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        ModelAndView model = new ModelAndView();

        if (error != null) {
            model.addObject("loginError", true);
        }

        model.setViewName("login");

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

    /**
     * Creates the new user and stored it in the database.
     *
     * @param newUser The new user to register
     * @return Login URL
     */
    @RequestMapping(value = "/register", method = POST)
    public ModelAndView createUser(@Valid DukeEncountersUser newUser, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("register", "formErrors", result.getAllErrors());
        }

        // TODO react on validation error
        DukeEncountersUser user = userService.createUser(newUser);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "User {} created", user);

        return new ModelAndView("login");
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(dukeEncountersUserValidator);
    }
}
