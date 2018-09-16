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
package de.dominikschadow.dukeencounters.search;

import de.dominikschadow.dukeencounters.encounter.EncounterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static de.dominikschadow.dukeencounters.TestData.twoTestEncounters;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the [@link SearchController} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SearchController.class)
public class SearchControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EncounterService encounterService;
    @MockBean
    private SearchFilterValidator validator;

    @Test
    @WithMockUser(username = "arthur@dent.com", password = "arthur@dent.com", roles = "USER")
    public void quickSearchEncounter() throws Exception {
        given(encounterService.getEncountersByEvent("JavaOne")).willReturn(twoTestEncounters());

        mvc.perform(post("/search").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("quickSearch", "JavaOne"))
                .andExpect(status().isOk())
                .andExpect(view().name("encounters"))
                .andExpect(model().attribute("encounters", hasSize(2)));
    }
}
