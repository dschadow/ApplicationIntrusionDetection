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
import de.dominikschadow.duke.encounters.domain.Encounter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.inject.Named;

/**
 * Validates an encounter: checks required fields and scans for basic Cross-Site Scripting and SQL Injection payload.
 *
 * @author Dominik Schadow
 */
@Named
public class EncounterValidator extends BaseEncounterValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Encounter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        Encounter encounter = (Encounter) target;

        errors = validateBaseData(encounter.getEvent(), encounter.getLocation(), encounter.getCountry(), errors);

        if (securityValidationService.hasXssPayload(encounter.getComment())) {
            fireXssEvent();
            errors.rejectValue("comment", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(encounter.getComment())) {
            fireSqlIEvent();
            errors.rejectValue("comment", Constants.SQLI_ERROR_CODE);
        }
    }
}
