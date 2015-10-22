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
import de.dominikschadow.duke.encounters.domain.Likelihood;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.services.SecurityValidationService;
import de.dominikschadow.duke.encounters.services.UserService;
import org.owasp.appsensor.core.DetectionPoint;
import org.owasp.appsensor.core.DetectionSystem;
import org.owasp.appsensor.core.Event;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SearchFilterValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilterValidator.class);
    @Autowired
    private SpringValidatorAdapter validator;
    @Autowired
    private EventManager ids;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityValidationService securityValidationService;
    @Autowired
    private DetectionSystem detectionSystem;

    @Override
    public boolean supports(Class<?> clazz) {
        return SearchFilter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        SearchFilter filter = (SearchFilter) target;

        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            if (securityValidationService.hasXssPayload(filter.getEvent())) {
                fireXssEvent();
                errors.rejectValue("event", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
            } else if (securityValidationService.hasSqlIPayload(filter.getEvent())) {
                fireSqlIEvent();
                errors.rejectValue("event", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
            }
        }

        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            if (securityValidationService.hasXssPayload(filter.getLocation())) {
                fireXssEvent();
                errors.rejectValue("location", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
            } else if (securityValidationService.hasSqlIPayload(filter.getLocation())) {
                fireSqlIEvent();
                errors.rejectValue("location", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
            }
        }

        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            if (securityValidationService.hasXssPayload(filter.getCountry())) {
                fireXssEvent();
                errors.rejectValue("country", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
            } else if (securityValidationService.hasSqlIPayload(filter.getCountry())) {
                fireSqlIEvent();
                errors.rejectValue("country", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
            }
        }

        if (filter.getYear() < 1995) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as year of the event - possible typo",
                    filter.getYear());
            errors.rejectValue("year", "typo", "Java did not exist before 1995");
        }

        if (!Strings.isNullOrEmpty(filter.getLikelihood())) {
            try {
                Likelihood.fromString(filter.getLikelihood());
            } catch (IllegalArgumentException ex) {
                LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured enum " +
                        "range", filter.getLikelihood());
                if (securityValidationService.hasXssPayload(filter.getLikelihood())) {
                    fireXssEvent();
                    errors.rejectValue("likelihood", Constants.XSS_ERROR_CODE, Constants.XSS_ERROR_MESSAGE);
                } else if (securityValidationService.hasSqlIPayload(filter.getLikelihood())) {
                    fireSqlIEvent();
                    errors.rejectValue("likelihood", Constants.SQLI_ERROR_CODE, Constants.SQLI_ERROR_MESSAGE);
                } else {
                    DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.ACCESS_CONTROL,
                            "ACE2-001");
                    ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
                    errors.rejectValue("likelihood", Constants.ATTACK_ERROR_CODE, "This is not a valid likelihood " +
                            "value");
                }
            }
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range",
                    filter.getConfirmations());
            errors.rejectValue("confirmations", Constants.ATTACK_ERROR_CODE, "No of confirmations out ot range");
        }
    }

    private void fireXssEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.INPUT_VALIDATION, "IE1-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }

    private void fireSqlIEvent() {
        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.COMMAND_INJECTION, "CIE1-001");
        ids.addEvent(new Event(userService.getUser(), detectionPoint, detectionSystem));
    }
}
