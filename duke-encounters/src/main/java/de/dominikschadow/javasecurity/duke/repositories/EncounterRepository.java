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
package de.dominikschadow.javasecurity.duke.repositories;

import de.dominikschadow.javasecurity.duke.domain.Encounter;
import de.dominikschadow.javasecurity.duke.domain.SearchFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    @Query(value="select e from Encounter e")
    List<Encounter> findWithPageable(Pageable latestTen);

    @Query(value="select e from Encounter e")
    List<Encounter> findByFilter(SearchFilter filter);
}
