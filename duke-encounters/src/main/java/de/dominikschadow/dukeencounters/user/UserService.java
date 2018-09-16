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
package de.dominikschadow.dukeencounters.user;

import de.dominikschadow.dukeencounters.account.PasswordChange;
import de.dominikschadow.dukeencounters.encounter.Authority;
import de.dominikschadow.dukeencounters.encounter.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.security.logging.SecurityMarkers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

/**
 * CRUD services for users.
 *
 * @author Dominik Schadow
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user. This user receives the normal user role, is enabled and has the {@link Level} NEWBIE.
     *
     * @param newUser The user to create
     * @return The created user with all fields filled
     */
    @Transactional
    public User createUser(@NotNull final User newUser) {
        log.info("Creating user with username {}", newUser.getEmail());
        Authority authority = authorityRepository.save(Authority.builder().username(newUser.getUsername()).authority("ROLE_USER").build());

        newUser.setEnabled(true);
        newUser.setLevel(Level.NEWBIE);
        newUser.setAuthority(authority);

        User user = userRepository.save(newUser);

        log.warn(SecurityMarkers.SECURITY_AUDIT, "Created a new user with username {} and id {} with role {}",
                user.getUsername(), user.getId(), user.getAuthority().getAuthority());

        return user;
    }

    @Transactional
    public User updateUser(@NotNull final User user, @NotNull final User updatedUser) {
        User currentUser = findUser(user.getUsername());

        currentUser.setFirstname(updatedUser.getFirstname());
        currentUser.setLastname(updatedUser.getLastname());
        currentUser.setEmail(updatedUser.getEmail());

        return userRepository.save(currentUser);
    }

    @Transactional
    public User updatePassword(@NotNull final User user, @NotNull final PasswordChange password) {
        User currentUser = findUser(user.getUsername());

        currentUser.setPassword(hashPassword(password.getNewPassword()));

        return userRepository.save(currentUser);
    }

    public boolean confirmPassword(@NotNull final String currentPassword, @NotNull final String updatedPassword) {
        return passwordEncoder.matches(currentPassword, updatedPassword);
    }

    public User findUser(@NotNull final String username) {
        return userRepository.findByUsername(username);
    }

    public org.owasp.appsensor.core.User getAppSensorUser(User user) {
        org.owasp.appsensor.core.User appSensorUser = new org.owasp.appsensor.core.User();
        appSensorUser.setUsername(user.getUsername());

        return appSensorUser;
    }

    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
