package de.dominikschadow.duke.encounters.controllers;

import de.dominikschadow.duke.encounters.DukeEncountersApplication;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests the [@link EncounterController} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DukeEncountersApplication.class)
@WebAppConfiguration
public class EncounterControllerTests {
    @Autowired
    private WebApplicationContext webContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void encountersPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/encounters"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("encounters"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("encounters"))
                .andExpect(MockMvcResultMatchers.model().attribute("encounters", Matchers.hasSize(20)));
    }
}
