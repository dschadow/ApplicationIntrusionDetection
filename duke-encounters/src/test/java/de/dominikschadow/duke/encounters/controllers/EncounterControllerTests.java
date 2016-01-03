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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc.perform(get("/encounters"))
                .andExpect(status().isOk())
                .andExpect(view().name("encounters"))
                .andExpect(model().attributeExists("encounters"))
                .andExpect(model().attribute("encounters", Matchers.hasSize(20)));
    }
}
