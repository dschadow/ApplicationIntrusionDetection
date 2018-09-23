/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
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

import de.dominikschadow.dukeencounters.config.DukeEncountersProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static de.dominikschadow.dukeencounters.TestData.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;

/**
 * Tests the [@link EncounterService} class.
 *
 * @author Dominik Schadow
 */
public class EncounterServiceTest {
    @Mock
    private EncounterRepository repository;
    @Mock
    private EventManager ids;
    @Mock
    private DetectionSystem detectionSystem;
    @Mock
    private DukeEncountersProperties properties;

    private EncounterService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new EncounterService(repository, ids, detectionSystem, properties);
    }

    @Test
    public void getLatestEncountersInsideLimitsShouldReturnList() {
        given(properties.getLatestAmount()).willReturn(2);
        given(repository.findWithPageable(any(Pageable.class))).willReturn(twoTestEncounters());
        List<Encounter> latestEncounters = service.getLatestEncounters("test");

        assertThat(latestEncounters.size()).isEqualTo(2);
    }

    @Test
    public void getLatestEncountersOutsideLimitsFiresSqlIEvent() {
        given(properties.getLatestAmount()).willReturn(2);
        given(repository.findWithPageable(any(Pageable.class))).willReturn(threeTestEncounters());
        List<Encounter> latestEncounters = service.getLatestEncounters("test");

        assertThat(latestEncounters.size()).isEqualTo(3);
    }

    @Test
    public void getEncountersByUsernameShouldReturnList() {
        given(repository.findAllByUsername(anyString())).willReturn(threeTestEncounters());
        List<Encounter> encounters = service.getEncountersByUsername("test");

        assertThat(encounters.size()).isEqualTo(3);
    }

    @Test
    public void getEncountersByEventShouldReturnList() {
        given(repository.findByEventContaining(anyString())).willReturn(threeTestEncounters());
        List<Encounter> encounters = service.getEncountersByEvent("test");

        assertThat(encounters.size()).isEqualTo(3);
    }

    @Test
    public void isOwnEncountersForOwnEncounterShouldReturnTrue() {
        given(repository.findByIdAndUsername(anyLong(), anyString())).willReturn(testEncounter(1));
        boolean ownEncounter = service.isOwnEncounter(1, "test");

        assertThat(ownEncounter).isTrue();
    }

    @Test
    public void isOwnEncountersForOtherEncounterShouldReturnFalse() {
        given(repository.findByIdAndUsername(anyLong(), anyString())).willReturn(null);
        boolean ownEncounter = service.isOwnEncounter(1, "test");

        assertThat(ownEncounter).isFalse();
    }
}