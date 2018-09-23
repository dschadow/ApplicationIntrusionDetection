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
package de.dominikschadow.dukeencounters.confirmation;

import de.dominikschadow.dukeencounters.encounter.Encounter;
import de.dominikschadow.dukeencounters.user.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Represents a confirmation of an encounter.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "confirmations")
@Data
@ToString(exclude = {"user", "encounter"})
public class Confirmation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "encounter_id", nullable = false)
    private Encounter encounter;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @PrePersist
    public void prePersist() {
        setDate(LocalDate.now());
    }
}
