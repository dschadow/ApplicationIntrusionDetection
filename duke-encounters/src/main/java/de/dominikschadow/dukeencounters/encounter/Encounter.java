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

import de.dominikschadow.dukeencounters.confirmation.Confirmation;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Represents a single encounter.
 *
 * @author Dominik Schadow
 */
@Entity
@Table(name = "encounters")
@Data
public class Encounter {
    @Id
    @GeneratedValue
    private long id;
    @NotBlank(message = "An event is required for each encounter")
    @Column(nullable = false)
    private String event;
    @NotBlank(message = "A location is required for each encounter")
    @Column(nullable = false)
    private String location;
    @NotBlank(message = "A country is required for each encounter")
    @Column(nullable = false)
    private String country;
    private String comment;
    @NotNull(message = "A date in the format MM/dd/yyyy is required for each encounter")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DukeEncountersUser user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "encounter")
    private List<Confirmation> confirmations;

    public String getLikelihood() {
        return Likelihood.getLikelihood(getConfirmations()).getName();
    }
}
