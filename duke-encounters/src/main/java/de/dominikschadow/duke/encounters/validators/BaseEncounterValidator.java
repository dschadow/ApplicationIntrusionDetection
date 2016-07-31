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
package de.dominikschadow.duke.encounters.validators;

import de.dominikschadow.duke.encounters.Constants;
import de.dominikschadow.duke.encounters.services.SecurityValidationService;
import de.dominikschadow.duke.encounters.services.UserService;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import static org.owasp.appsensor.core.DetectionPoint.Category.COMMAND_INJECTION;
import static org.owasp.appsensor.core.DetectionPoint.Category.INPUT_VALIDATION;

/**
 * Base validator class for encounter common properties like event, location and country.
 *
 * @author Dominik Schadow
 */
public abstract class BaseEncounterValidator implements Validator {
    @Autowired
    protected SpringValidatorAdapter validator;
    @Autowired
    protected SecurityValidationService securityValidationService;
    @Autowired
    protected EventManager ids;
    @Autowired
    protected UserService userService;
    @Autowired
    protected DetectionSystem detectionSystem;

    protected Errors validateBaseData(String event, String location, String country, Errors errors) {
        if (securityValidationService.hasXssPayload(event)) {
            fireXssEvent();
            errors.rejectValue("event", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(event)) {
            fireSqlIEvent();
            errors.rejectValue("event", Constants.SQLI_ERROR_CODE);
        }

        if (securityValidationService.hasXssPayload(location)) {
            fireXssEvent();
            errors.rejectValue("location", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(location)) {
            fireSqlIEvent();
            errors.rejectValue("location", Constants.SQLI_ERROR_CODE);
        }

        if (securityValidationService.hasXssPayload(country)) {
            fireXssEvent();
            errors.rejectValue("country", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(country)) {
            fireSqlIEvent();
            errors.rejectValue("country", Constants.SQLI_ERROR_CODE);
        }

        return errors;
    }

    protected void fireXssEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(INPUT_VALIDATION, "IE1-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }

    protected void fireSqlIEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(COMMAND_INJECTION, "CIE1-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }
}
