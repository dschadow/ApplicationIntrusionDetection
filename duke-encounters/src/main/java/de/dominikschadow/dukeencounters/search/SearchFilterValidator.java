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
package de.dominikschadow.dukeencounters.search;

import de.dominikschadow.dukeencounters.Constants;
import de.dominikschadow.dukeencounters.encounter.BaseEncounterValidator;
import de.dominikschadow.dukeencounters.encounter.Likelihood;
import de.dominikschadow.dukeencounters.security.SecurityValidationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * Validates a search filter: scans for basic Cross-Site Scripting and SQL Injection payload.
 *
 * @author Dominik Schadow
 */
@Component
@Slf4j
public class SearchFilterValidator extends BaseEncounterValidator {
    public SearchFilterValidator(EventManager ids, DetectionSystem detectionSystem, SpringValidatorAdapter validator,
                                 SecurityValidationService securityValidationService) {
        super(validator, securityValidationService, ids, detectionSystem);
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return SearchFilter.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        validator.validate(target, errors);

        SearchFilter filter = (SearchFilter) target;

        // FIXME find username
        String username = "";

        validateBaseData(username, filter.getEvent(), filter.getLocation(), filter.getCountry(), errors);

        if (securityValidationService.hasXssPayload(filter.getYear())) {
            fireXssEvent(username);
            errors.rejectValue("year", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(filter.getYear())) {
            fireSqlIEvent(username);
            errors.rejectValue("year", Constants.SQLI_ERROR_CODE);
        } else if (StringUtils.isNotEmpty(filter.getYear())) {
            if (StringUtils.isNumeric(filter.getYear())) {
                int year = Integer.parseInt(filter.getYear());

                if (year < Constants.YEAR_OF_JAVA_CREATION) {
                    log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as event year - possible typo",
                            filter.getYear());
                    errors.rejectValue("year", Constants.INVALID_YEAR_ERROR_CODE);
                }
            } else {
                errors.rejectValue("year", Constants.INVALID_YEAR_ERROR_CODE);
            }
        }

        try {
            Likelihood.fromString(filter.getLikelihood());
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage(), ex);
            log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured range",
                    filter.getLikelihood());
            fireInvalidValueEvent(username);
            errors.rejectValue("likelihood", Constants.ATTACK_ERROR_CODE);
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range",
                    filter.getConfirmations());
            fireInvalidValueEvent(username);
            errors.rejectValue("confirmations", Constants.ATTACK_ERROR_CODE);
        }
    }
}
