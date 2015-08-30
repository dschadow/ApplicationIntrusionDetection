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

        userRole = roleRepository.findByRolename("user");
    }

    /**
     * Creates a new user. This user receives the normal user role, is enabled and has the {@link Level} NEWBIE.
     *
     * @param register The user to created
     * @return The created user with all fields filled
     */
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
