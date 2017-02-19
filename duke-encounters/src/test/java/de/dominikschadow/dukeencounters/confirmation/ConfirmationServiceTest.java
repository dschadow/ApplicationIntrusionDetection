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

import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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

    private Confirmation testConfirmation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ConfirmationService(repository, userService, encounterService);

        testConfirmation = new Confirmation();
        testConfirmation.setId(1);
    }

    @Test
    public void getConfirmationsByUsernameWhenUsernameIsNullShouldThrowException() throws Exception {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("username");
        service.getConfirmationsByUsername(null);
    }

    @Test
    public void getConfirmationByUsernameAndEncounterIdWhenUsernameAndIdAreValidReturnsConfirmation() throws Exception {
        given(repository.findByUsernameAndEncounterId("test", 1)).willReturn(testConfirmation);
        Confirmation confirmation = service.getConfirmationByUsernameAndEncounterId("test", 1);

        assertThat(confirmation.getId()).isEqualTo(1);
    }



}