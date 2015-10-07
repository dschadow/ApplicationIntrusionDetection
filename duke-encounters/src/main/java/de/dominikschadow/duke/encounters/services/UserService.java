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
package de.dominikschadow.duke.encounters.services;

import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.domain.Level;
import de.dominikschadow.duke.encounters.domain.Role;
import de.dominikschadow.duke.encounters.repositories.RoleRepository;
import de.dominikschadow.duke.encounters.repositories.UserRepository;
import org.owasp.appsensor.core.User;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * CRUD services for users.
 *
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

        userRole = roleRepository.findByName("user");
    }

    /**
     * Creates a new user. This user receives the normal user role, is enabled and has the {@link Level} NEWBIE.
     *
     * @param newUser The user to create
     * @return The created user with all fields filled
     */
    public DukeEncountersUser createUser(@NotNull DukeEncountersUser newUser) {
        LOGGER.info("Creating user with username {}", newUser.getEmail());

        newUser.setEnabled(true);
        newUser.setLevel(Level.NEWBIE);
        newUser.setRegistrationDate(new Date());

        LOGGER.info("Setting role {} for user {}", userRole, newUser.getEmail());
        newUser.setRole(userRole);

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            LOGGER.error("User with username {} already exists", newUser.getUsername());
            return null;
        }

        DukeEncountersUser user = userRepository.save(newUser);

        LOGGER.info(SecurityMarkers.SECURITY_AUDIT, "Created a new user with username {} and id {} with role {}",
                user.getUsername(), user.getId(), user.getRole());

        return user;
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getUser() {
        return new User(getUsername());
    }
}
