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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.dominikschadow.dukeencounters.TestData.twoTestEncounters;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the [@link EncounterController} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringRunner.class)
@WebMvcTest(EncounterController.class)
public class EncounterControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EncounterService encounterService;
    @MockBean
    private EncounterValidator encounterValidator;
    @MockBean
    private DetectionSystem detectionSystem;
    @MockBean
    private EventManager eventManager;

    @Test
    @WithMockUser(username = "arthur@dent.com", password = "arthur@dent.com", roles = "USER")
    public void listEncounters() throws Exception {
        given(encounterService.getEncounters(anyString(), anyString())).willReturn(twoTestEncounters());

        mvc.perform(get("/encounters"))
                .andExpect(status().isOk())
                .andExpect(view().name("encounters"))
                .andExpect(model().attributeExists("encounters"))
                .andExpect(model().attribute("encounters", hasSize(2)));
    }
}
