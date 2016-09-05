/*
 * Copyright (C) 2016 Dominik Schadow, dominikschadow@gmail.com
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

import de.dominikschadow.duke.encounters.domain.Authority;
import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
import de.dominikschadow.duke.encounters.enums.Level;
import de.dominikschadow.duke.encounters.repositories.AuthorityRepository;
import de.dominikschadow.duke.encounters.repositories.UserRepository;
import org.owasp.appsensor.core.User;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * CRUD services for users.
 *
 * @author Dominik Schadow
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final AuthorityRepository authorityRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user. This user receives the normal user role, is enabled and has the {@link Level} NEWBIE.
     *
     * @param newUser The user to create
     * @return The created user with all fields filled
     */
    @Transactional
    public DukeEncountersUser createUser(@NotNull final DukeEncountersUser newUser) {
        logger.info("Creating user with username {}", newUser.getEmail());
        Authority authority = authorityRepository.save(new Authority(newUser.getUsername(), "ROLE_USER"));

        newUser.setEnabled(true);
        newUser.setLevel(Level.NEWBIE);
        newUser.setRegistrationDate(new Date());
        newUser.setAuthority(authority);
        newUser.setPassword(hashPassword(newUser.getPassword()));

        DukeEncountersUser user = userRepository.save(newUser);

        logger.warn(SecurityMarkers.SECURITY_AUDIT, "Created a new user with username {} and id {} with role {}",
                user.getUsername(), user.getId(), user.getAuthority().getAuthority());

        return user;
    }

    @Transactional
    public DukeEncountersUser updateUser(@NotNull final DukeEncountersUser dukeEncountersUser) {
        return userRepository.save(dukeEncountersUser);
    }

    public boolean confirmPassword(@NotNull final String password) {
        return passwordEncoder.matches(password, getDukeEncountersUser().getPassword());
    }

    public DukeEncountersUser findUser(final String username) {
        return userRepository.findByUsername(username);
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        if (authentication instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication;

            username = userDetails.getUsername();
        }

        return username;
    }

    public User getUser() {
        return new User(getUsername());
    }

    public DukeEncountersUser getDukeEncountersUser() {
        return getDukeEncountersUser(getUsername());
    }

    public DukeEncountersUser getDukeEncountersUser(@NotNull final String username) {
        return userRepository.findByUsername(username);
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
