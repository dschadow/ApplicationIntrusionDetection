package de.dominikschadow.duke.encounters.services;

import de.dominikschadow.duke.encounters.domain.Level;
import de.dominikschadow.duke.encounters.domain.Role;
import de.dominikschadow.duke.encounters.domain.User;
import de.dominikschadow.duke.encounters.repositories.RoleRepository;
import de.dominikschadow.duke.encounters.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Dominik Schadow
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private final Role userRole;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

        userRole = roleRepository.findByRolename("user");
    }

    public User createUser(User register) {
        LOGGER.info("Creating user with username {}", register.getEmail());

        register.setUsername(register.getEmail());
        register.setEnabled(true);
        register.setLevel(Level.NEWBIE);
        register.setRegistrationDate(new Date());

        LOGGER.info("Setting role {} for user {}", userRole, register.getEmail());
        register.setRole(userRole);

        register.setPassword(passwordEncoder.encode(register.getPassword()));

        if (userRepository.findByUsername(register.getUsername()) != null) {
            return null;
        }

        User user = userRepository.save(register);

        return user;
    }
}
