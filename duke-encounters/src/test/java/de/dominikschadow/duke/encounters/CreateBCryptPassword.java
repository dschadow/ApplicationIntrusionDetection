/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Creates BCrypt encoded passwords.
 *
 * @author Dominik Schadow
 */
public class CreateBCryptPassword {
    public static void main(String[] args) {
        List<String> usernames = Arrays.asList("arthur@dent.com", "ford@prefect.com", "zaphod@beeblebrox.com",
                "marvin@marvin.com", "humma@kavula.com", "questular@rontok.com", "deep@thought.com", "tricia@mcmillan" +
                        ".com", "slartibartfast@slartibartfast.com", "jin@jenz.com", "gag@halfrunt.com");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        for (String username : usernames) {
            String hashedPassword = passwordEncoder.encode(username);

            System.out.println(hashedPassword);
        }
    }
}
