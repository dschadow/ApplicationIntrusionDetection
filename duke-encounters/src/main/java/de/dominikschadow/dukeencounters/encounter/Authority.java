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

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Holds a role in the application.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Data
@Builder
@ToString
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Username may not be null")
    private String username;
    @NotBlank(message = "Authority may not be null")
    private String authority;
}
