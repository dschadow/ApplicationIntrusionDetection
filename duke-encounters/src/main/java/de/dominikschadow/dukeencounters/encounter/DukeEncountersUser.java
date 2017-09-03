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
package de.dominikschadow.dukeencounters.encounter;

import de.dominikschadow.dukeencounters.user.Level;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
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
@Data
@ToString(exclude = {"password", "confirmPassword"})
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
    @Email
    @Column(nullable = false)
    private String email;
    @Length(min = 10, max = 1024, message = "A password is required and must contain between 10 and 60 characters")
    @Column(nullable = false, length = 60)
    private String password;
    @Transient
    private String confirmPassword;
    private Date registrationDate;
    @Enumerated(EnumType.STRING)
    private Level level;
    private boolean enabled;
    @OneToOne
    private Authority authority;
}
