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
package de.dominikschadow.duke.encounters;

import de.dominikschadow.duke.encounters.DukeEncountersApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Selenium tests for the web application.
 *
 * @author Dominik Schadow
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DukeEncountersApplication.class)
@WebIntegrationTest
public class ServerWebTests {
    private static FirefoxDriver browser;

    @BeforeClass
    public static void init() {
        browser = new FirefoxDriver();
        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void tearDown() {
        browser.quit();
    }

    @Test
    public void openHomepage() {
        browser.get("http://localhost:8080");

        WebElement welcome = browser.findElement(By.id("welcome"));
        assertTrue(welcome.getText().contains("Duke Encounters"));
    }
}
