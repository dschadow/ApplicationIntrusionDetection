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
package de.dominikschadow.dukeencounters.confirmation;

import com.google.common.collect.Lists;
import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static de.dominikschadow.dukeencounters.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;

/**
 * Tests the [@link ConfirmationService} class.
 *
 * @author Dominik Schadow
 */
public class ConfirmationServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ConfirmationRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private EncounterService encounterService;

    private ConfirmationService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ConfirmationService(repository, userService, encounterService);
    }

    @Test
    public void getConfirmationsByUsernameWhenUsernameIsNullShouldThrowException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("username");
        service.getConfirmationsByUsername(null);
    }

    @Test
    public void getConfirmationsByUsernameWhenUsernameIsValidShouldReturnConfirmations() {
        given(repository.findAllByUsername(anyString())).willReturn(Lists.newArrayList(testConfirmation()));
        List<Confirmation> confirmations = service.getConfirmationsByUsername("test");

        assertThat(confirmations.size()).isEqualTo(1);
    }

    @Test
    public void getConfirmationByUsernameAndEncounterIdWhenUsernameAndIdAreValidShouldReturnConfirmation() {
        given(repository.findByUsernameAndEncounterId(anyString(), anyLong())).willReturn(testConfirmation());
        Confirmation confirmation = service.getConfirmationByUsernameAndEncounterId("test", 1);

        assertThat(confirmation.getId()).isEqualTo(1);
    }

    @Test
    public void getOwnConfirmationsShouldReturnConfirmations() {
        given(repository.findAllByUsername(anyString())).willReturn(Lists.newArrayList(testConfirmation()));
        List<Confirmation> confirmations = service.getConfirmations("own");

        assertThat(confirmations.size()).isEqualTo(1);
    }

    @Test
    public void getConfirmationsWithTypeNullShouldReturnAllConfirmations() {
        given(repository.findAll()).willReturn(Lists.newArrayList(testConfirmation()));
        List<Confirmation> confirmations = service.getConfirmations(null);

        assertThat(confirmations.size()).isEqualTo(1);
    }

    @Test
    public void hasConfirmedEncounterForAlreadyConfirmedEncounterShouldReturnTrue() {
        given(repository.findByUsernameAndEncounterId(anyString(), anyLong())).willReturn(testConfirmation());
        boolean hasConfirmedEncounter = service.hasConfirmedEncounter("test", 1);

        assertThat(hasConfirmedEncounter).isTrue();
    }

    @Test
    public void hasConfirmedEncounterForUnconfirmedEncounterShouldReturnFalse() {
        given(repository.findByUsernameAndEncounterId(anyString(), anyLong())).willReturn(null);
        boolean hasConfirmedEncounter = service.hasConfirmedEncounter("test", 1);

        assertThat(hasConfirmedEncounter).isFalse();
    }

    @Test
    public void deleteEncounterForOwnEncounterShouldSucceed() {
        doNothing().when(repository).deleteById(1l);
        service.deleteConfirmation("test", 1);
    }

    @Test
    public void addConfirmationWithValidDataShouldSucceed() {
        given(userService.getDukeEncountersUser(anyString())).willReturn(testUser());
        given(encounterService.getEncounterById(anyLong())).willReturn(testEncounter(1));
        given(repository.save(any(Confirmation.class))).willReturn(testConfirmation());
        Confirmation confirmation = service.addConfirmation("test", 1);

        assertThat(confirmation.getEncounter().getId()).isEqualTo(1);
        assertThat(confirmation.getUser().getUsername()).isEqualTo("test");
    }
}