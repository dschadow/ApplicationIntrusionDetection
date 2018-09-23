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

import de.dominikschadow.dukeencounters.confirmation.Confirmation;
import de.dominikschadow.dukeencounters.user.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a single encounter.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "encounters")
@Data
@ToString
public class Encounter {
    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 3, max = 255, message = "Event is required and must be between {min} and {max} characters long")
    @Column(nullable = false)
    private String event;

    @Size(min = 3, max = 255, message = "Location is required and must be between {min} and {max} characters long")
    @Column(nullable = false)
    private String location;

    @Size(min = 3, max = 255, message = "Country is required and must be between {min} and {max} characters long")
    @Column(nullable = false)
    private String country;

    @Size(max = 1024, message = "Comment can not contain more than {max} characters")
    @Column(length = 1024)
    private String comment;

    @NotNull(message = "A date in the format MM/dd/yyyy is required for each encounter")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    @Past(message = "Encounter date must be in the past")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encounter")
    private List<Confirmation> confirmations;

    public String getLikelihood() {
        return Likelihood.getLikelihood(getConfirmations()).getName();
    }
}
