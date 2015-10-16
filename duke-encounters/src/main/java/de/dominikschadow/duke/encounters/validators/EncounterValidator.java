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

import com.google.common.base.Strings;
import de.dominikschadow.duke.encounters.Constants;
import de.dominikschadow.duke.encounters.detection.IntrusionDetectionService;
import de.dominikschadow.duke.encounters.domain.Encounter;
import de.dominikschadow.duke.encounters.services.SecurityValidationService;
import de.dominikschadow.duke.encounters.services.UserService;
import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.inject.Named;

/**
 * Validates an encounter: checks required fields and scans for basic Cross-Site Scripting and SQL Injection payload.
 *
 * @author Dominik Schadow
 */
@Named
public class EncounterValidator implements Validator {
    @Autowired
    private SpringValidatorAdapter validator;
    @Autowired
    private AppSensorClient appSensorClient;
    @Autowired
    private EventManager ids;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityValidationService securityValidationService;
    @Autowired
    private IntrusionDetectionService intrusionDetectionService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Encounter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "event", "required.event", "The event is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "location", "required.location", "The location is required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country", "required.country", "The country is required");

        Encounter encounter = (Encounter) target;

        if (securityValidationService.hasXssPayload(encounter.getEvent())) {
            fireXssEvent();
            errors.rejectValue("event", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
        } else if (securityValidationService.hasSqlIPayload(encounter.getEvent())) {
            fireSqlIEvent();
            errors.rejectValue("event", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
        }

        if (securityValidationService.hasXssPayload(encounter.getLocation())) {
            fireXssEvent();
            errors.rejectValue("location", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
        } else if (securityValidationService.hasSqlIPayload(encounter.getLocation())) {
            fireSqlIEvent();
            errors.rejectValue("location", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
        }

        if (securityValidationService.hasXssPayload(encounter.getCountry())) {
            fireXssEvent();
            errors.rejectValue("country", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
        } else if (securityValidationService.hasSqlIPayload(encounter.getCountry())) {
            fireSqlIEvent();
            errors.rejectValue("country", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
        }

        if (!Strings.isNullOrEmpty(encounter.getComment())) {
            if (securityValidationService.hasXssPayload(encounter.getComment())) {
                fireXssEvent();
                errors.rejectValue("comment", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
            } else if (securityValidationService.hasSqlIPayload(encounter.getComment())) {
                fireSqlIEvent();
                errors.rejectValue("comment", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
            }
        }
    }

    private void fireXssEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.INPUT_VALIDATION, "IE1");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, intrusionDetectionService.getDetectionSystem()));
    }

    private void fireSqlIEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.COMMAND_INJECTION, "CIE1");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, intrusionDetectionService.getDetectionSystem()));
    }
}
