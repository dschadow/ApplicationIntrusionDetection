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
package de.dominikschadow.duke.encounters.domain;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.Date;

/**
 * Holds a user of the application.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class DukeEncountersUser {
    @Id
    @GeneratedValue
    private long id;
    @NotBlank(message = "Your firstname is required")
    private String firstname;
    @NotBlank(message = "Your lastname is required")
    private String lastname;
    @NotBlank(message = "Your username is required")
    @Column(nullable = false)
    private String username;
    @NotBlank(message = "Your email address is required")
    @Column(nullable = false)
    private String email;
    @NotBlank(message = "A password is required")
    @Column(nullable = false, length = 60)
    private String password;
    @Transient
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;
    private Date registrationDate;
    @Enumerated(EnumType.STRING)
    private Level level;
    private boolean enabled;
    @OneToOne
    private Authority authority;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        String name = Strings.nullToEmpty(getFirstname()) + " " + Strings.nullToEmpty(getLastname());
        name = CharMatcher.WHITESPACE.trimTrailingFrom(name);

        return name;
    }
}
