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
package de.dominikschadow.dukeencounters.search;

import com.google.common.base.Strings;
import de.dominikschadow.dukeencounters.Constants;
import de.dominikschadow.dukeencounters.encounter.BaseEncounterValidator;
import de.dominikschadow.dukeencounters.encounter.Likelihood;
import de.dominikschadow.dukeencounters.security.SecurityValidationService;
import de.dominikschadow.dukeencounters.user.UserService;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.inject.Named;

/**
 * Validates a search filter: scans for basic Cross-Site Scripting and SQL Injection payload.
 *
 * @author Dominik Schadow
 */
@Named
public class SearchFilterValidator extends BaseEncounterValidator implements Validator {
    private static final Logger logger = LoggerFactory.getLogger(SearchFilterValidator.class);

    public SearchFilterValidator(EventManager ids, DetectionSystem detectionSystem, SpringValidatorAdapter validator,
                              UserService userService, SecurityValidationService securityValidationService) {
        super(ids, detectionSystem, validator, userService, securityValidationService);
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return SearchFilter.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        validator.validate(target, errors);

        SearchFilter filter = (SearchFilter) target;

        validateBaseData(filter.getEvent(), filter.getLocation(), filter.getCountry(), errors);

        if (securityValidationService.hasXssPayload(filter.getYear())) {
            fireXssEvent();
            errors.rejectValue("year", Constants.XSS_ERROR_CODE);
        } else if (securityValidationService.hasSqlIPayload(filter.getYear())) {
            fireSqlIEvent();
            errors.rejectValue("year", Constants.SQLI_ERROR_CODE);
        } else if (!Strings.isNullOrEmpty(filter.getYear())) {
            try {
                int year = Integer.parseInt(filter.getYear());

                if (year < Constants.YEAR_OF_JAVA_CREATION) {
                    logger.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as event year - possible typo",
                            filter.getYear());
                    errors.rejectValue("year", Constants.INVALID_YEAR_ERROR_CODE);
                }
            } catch (NumberFormatException ex) {
                logger.error(ex.getMessage(), ex);
                errors.rejectValue("year", Constants.INVALID_YEAR_ERROR_CODE);
            }
        }

        try {
            Likelihood.fromString(filter.getLikelihood());
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage(), ex);
            logger.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured range",
                    filter.getLikelihood());
            fireInvalidValueEvent();
            errors.rejectValue("likelihood", Constants.ATTACK_ERROR_CODE);
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            logger.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range",
                    filter.getConfirmations());
            fireInvalidValueEvent();
            errors.rejectValue("confirmations", Constants.ATTACK_ERROR_CODE);
        }
    }
}
