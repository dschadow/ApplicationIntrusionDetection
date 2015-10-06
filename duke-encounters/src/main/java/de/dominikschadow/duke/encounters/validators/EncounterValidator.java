/*
 * Copyright (C) 2015 Dominik Schadow, dominikschadow@gmail.com
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
package de.dominikschadow.duke.encounters.validators;

import de.dominikschadow.duke.encounters.domain.Encounter;
import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * @author Dominik Schadow
 */
@Named
public class EncounterValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncounterValidator.class);

    private SpringValidatorAdapter validator;
    private DetectionSystem detectionSystem;

    @Autowired
    private javax.validation.Validator jsr303Validator;

    @Autowired
    private AppSensorClient appSensorClient;

    @Autowired
    private EventManager ids;

    @PostConstruct
    public void init() {
        validator = new SpringValidatorAdapter(jsr303Validator);
        detectionSystem = new DetectionSystem(
                appSensorClient.getConfiguration().getServerConnection()
                        .getClientApplicationIdentificationHeaderValue());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Encounter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        Encounter encounter = (Encounter) target;

    }
}
