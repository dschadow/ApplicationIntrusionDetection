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
package de.dominikschadow.duke.encounters.repositories;

import de.dominikschadow.duke.encounters.domain.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Confirmation repository class for all {@link Confirmation} related queries.
 *
 * @author Dominik Schadow
 */
public interface ConfirmationRepository extends JpaRepository<Confirmation, Long>, JpaSpecificationExecutor {
    @Query(value = "select c from Confirmation c, DukeEncountersUser u where c.user = u.id and u.username = :username")
    List<Confirmation> findAllByUsername(@Param("username") String username);

    @Query(value = "select c from Confirmation c, DukeEncountersUser u where c.user = u.id and u.username = :username" +
            " and c.encounter = :encounterId")
    Confirmation findByUsernameAndEncounterId(@Param("username") String username, @Param("encounterId") long
            encounterId);
}
