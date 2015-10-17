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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "encounters")
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
    @NotNull(message = "A date in the format YYYY-MM-dd is required for each encounter")
    @Column(nullable = false)
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DukeEncountersUser user;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "encounter")
    private List<Confirmation> confirmations;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DukeEncountersUser getUser() {
        return user;
    }

    public void setUser(DukeEncountersUser user) {
        this.user = user;
    }

    public List<Confirmation> getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(List<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }

    public String getLikelihood() {
        return Likelihood.getLikelihood(getConfirmations()).toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", getId()).
                append("event", getEvent()).
                append("location", getLocation()).
                append("country", getCountry()).
                append("comment", getComment()).
                append("date", getDate()).
                append("user", getUser()).
                append("confirmations", getConfirmations()).
                append("likelihood", getLikelihood()).
                toString();
    }
}
