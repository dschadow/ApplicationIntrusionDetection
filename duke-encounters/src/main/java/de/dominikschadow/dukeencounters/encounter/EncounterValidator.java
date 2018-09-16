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
package de.dominikschadow.dukeencounters.encounter;

import de.dominikschadow.dukeencounters.Constants;
import de.dominikschadow.dukeencounters.security.SecurityValidationService;
import de.dominikschadow.dukeencounters.user.UserService;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * Validates an encounter: checks required fields and scans for basic Cross-Site Scripting and SQL Injection payload.
 *
 * @author Dominik Schadow
 */
@Component
public class EncounterValidator extends BaseEncounterValidator {
    public EncounterValidator(EventManager ids, DetectionSystem detectionSystem, SpringValidatorAdapter validator,
                              UserService userService, SecurityValidationService securityValidationService) {
        super(validator, securityValidationService, ids, userService, detectionSystem);
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return Encounter.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        validator.validate(target, errors);

        Encounter encounter = (Encounter) target;

        validateBaseData(encounter.getEvent(), encounter.getLocation(), encounter.getCountry(), errors);

        if (securityValidationService.hasXssPayload(encounter.getComment())) {
            fireXssEvent();
            errors.rejectValue("comment", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(encounter.getComment())) {
            fireSqlIEvent();
            errors.rejectValue("comment", Constants.SQLI_ERROR_CODE);
        }
    }
}
