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
package de.dominikschadow.dukeencounters.encounter;

import de.dominikschadow.dukeencounters.user.Level;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

/**
 * Holds a user of the application.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Builder
@ToString(exclude = {"password"})
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    @Length(min = 3, max = 255, message = "Firstname is required and must be between 3 and 255 characters long")
    @Column(nullable = false)
    private String firstname;
    @Length(min = 3, max = 255, message = "Lastname is required and must be between 3 and 255 characters long")
    @Column(nullable = false)
    private String lastname;
    @Length(min = 3, max = 255, message = "Username is required and must be between 3 and 255 characters long")
    @Column(nullable = false)
    private String username;
    @Email(message = "E-mail address is required")
    @Column(nullable = false)
    private String email;
    @Length(min = 10, max = 1024, message = "Password is required and must be between 10 and 1024 characters long")
    @Column(nullable = false, length = 60)
    private String password;
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    private Level level;
    private boolean enabled;
    @OneToOne
    private Authority authority;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        setRegistrationDate(LocalDateTime.now());
    }
}
