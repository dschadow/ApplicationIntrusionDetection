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
package de.dominikschadow.duke.encounters.controller;

import de.dominikschadow.duke.encounters.DukeEncountersApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests the [@link EncounterController} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DukeEncountersApplication.class)
@WebAppConfiguration
public class EncounterControllerTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void listEncounters() throws Exception {
        mvc.perform(get("/encounters"))
                .andExpect(status().isOk())
                .andExpect(view().name("encounters"))
                .andExpect(model().attributeExists("encounters"))
                .andExpect(model().attribute("encounters", hasSize(20)));
    }

    @Test
    public void searchEncounter() throws Exception {
        mvc.perform(post("/encounters").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("event", "JavaOne 2015")
                .param("location", "San Francisco")
                .param("likelihood", "ANY")
                .param("country", "USA"))
                .andExpect(status().isOk())
                .andExpect(view().name("encounters"))
                .andExpect(model().attribute("encounters", hasSize(1)));
    }
}
