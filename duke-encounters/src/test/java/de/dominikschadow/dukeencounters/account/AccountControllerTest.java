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
package de.dominikschadow.dukeencounters.account;

import de.dominikschadow.dukeencounters.confirmation.ConfirmationService;
import de.dominikschadow.dukeencounters.encounter.EncounterService;
import de.dominikschadow.dukeencounters.user.DukeEncountersUserValidator;
import de.dominikschadow.dukeencounters.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests the [@link AccountController} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EncounterService encounterService;
    @MockBean
    private UserService userService;
    @MockBean
    private ConfirmationService confirmationService;
    @MockBean
    private DukeEncountersUserValidator dukeEncountersUserValidator;

    @Test
    @WithMockUser(username = "arthur@dent.com", password = "arthur@dent.com", roles = "USER")
    public void verifyAccountAuthorizeOK() throws Exception {
        mvc.perform(get("/account")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "arthur@dent.com", password = "arthur@dent.com", roles = "DUMMY")
    public void verifyAccountAuthorizeNOK() throws Exception {
        mvc.perform(get("/account")).andExpect(status().isForbidden());
    }
}
