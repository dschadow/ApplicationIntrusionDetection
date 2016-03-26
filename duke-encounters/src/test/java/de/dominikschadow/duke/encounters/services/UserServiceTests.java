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
package de.dominikschadow.duke.encounters.services;

import de.dominikschadow.duke.encounters.DukeEncountersApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

/**
 * Tests the [@link UserService} class.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DukeEncountersApplication.class)
public class UserServiceTests {
    @Autowired
    private UserService userService;
    private List<String> passwords = Arrays.asList("arthur@dent.com", "ford@prefect.com", "zaphod@beeblebrox.com",
            "marvin@marvin.com", "humma@kavula.com", "questular@rontok.com", "deep@thought.com", "tricia@mcmillan.com",
            "slartibartfast@slartibartfast.com", "jin@jenz.com", "gag@halfrunt.com", "duke@encounters.com");

    @Test
    public void hashPassword() {
        for (String password : passwords) {
            String hashedPassword = userService.hashPassword(password);

            assertNotEquals(hashedPassword, password);
        }
    }
}
