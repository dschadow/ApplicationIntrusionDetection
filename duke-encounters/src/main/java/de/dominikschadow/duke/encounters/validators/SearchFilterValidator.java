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
import de.dominikschadow.duke.encounters.domain.Likelihood;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.owasp.appsensor.core.*;
import org.owasp.appsensor.core.event.EventManager;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Named;

/**
 * @author Dominik Schadow
 */
@Named
public class SearchFilterValidator implements Validator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchFilterValidator.class);
    private static final String XSS_ERROR_MESSAGE = "This application is XSS bulletproof";
    private static final String SQLI_ERROR_MESSAGE = "This application is SQL Injection bulletproof";
    private static final String XSS_ERROR_CODE = "xss.attempt";
    private static final String SQLI_ERROR_CODE = "sqli.attempt";
    private static final String ATTACK_ERROR_CODE = "attack.attempt";

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
        return SearchFilter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        SearchFilter filter = (SearchFilter) target;

        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            if (hasXssPayload(filter.getEvent())) {
                fireXssEvent();
                errors.rejectValue("event", XSS_ERROR_CODE, XSS_ERROR_MESSAGE);
            } else if (hasSqlIPayload(filter.getEvent())) {
                fireSqlIEvent();
                errors.rejectValue("event", SQLI_ERROR_CODE, SQLI_ERROR_MESSAGE);
            }
        }

        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            if (hasXssPayload(filter.getEvent())) {
                fireXssEvent();
                errors.rejectValue("location", XSS_ERROR_CODE, XSS_ERROR_MESSAGE);
            } else if (hasSqlIPayload(filter.getEvent())) {
                fireSqlIEvent();
                errors.rejectValue("location", SQLI_ERROR_CODE, SQLI_ERROR_MESSAGE);
            }
        }

        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            if (hasXssPayload(filter.getEvent())) {
                fireXssEvent();
                errors.rejectValue("country", XSS_ERROR_CODE, XSS_ERROR_MESSAGE);
            } else if (hasSqlIPayload(filter.getEvent())) {
                fireSqlIEvent();
                errors.rejectValue("country", SQLI_ERROR_CODE, SQLI_ERROR_MESSAGE);
            }
        }

        if (filter.getYear() > 0) {
            if (filter.getYear() < 1995) {
                LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as year of the event - possible typo",
                        filter.getYear());
                errors.rejectValue("year", "typo", "Java did not exist before 1995");
            }
        }

        if (!Strings.isNullOrEmpty(filter.getLikelihood())) {
            try {
                Likelihood likelihood = Likelihood.fromString(filter.getLikelihood());
            } catch (IllegalArgumentException ex) {
                LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured enum " +
                        "range", filter.getLikelihood());
                if (hasXssPayload(filter.getEvent())) {
                    fireXssEvent();
                    errors.rejectValue("likelihood", XSS_ERROR_CODE, XSS_ERROR_MESSAGE);
                } else if (hasSqlIPayload(filter.getEvent())) {
                    fireSqlIEvent();
                    errors.rejectValue("likelihood", SQLI_ERROR_CODE, SQLI_ERROR_MESSAGE);
                } else {
                    errors.rejectValue("likelihood", ATTACK_ERROR_CODE, "This is not a valid likelihood value");
                }
            }
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range",
                    filter.getConfirmations());
            if (hasXssPayload(filter.getEvent())) {
                fireXssEvent();
                errors.rejectValue("confirmations", XSS_ERROR_CODE, XSS_ERROR_MESSAGE);
            } else if (hasSqlIPayload(filter.getEvent())) {
                fireSqlIEvent();
                errors.rejectValue("confirmations", SQLI_ERROR_CODE, SQLI_ERROR_MESSAGE);
            } else {
                errors.rejectValue("confirmations", ATTACK_ERROR_CODE, "No of confirmations out ot range");
            }
        }
    }

    private boolean hasXssPayload(String payload) {
        return StringUtils.contains(payload, "<script>");
    }

    private boolean hasSqlIPayload(String payload) {
        return false;
    }

    private void fireXssEvent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User(authentication.getName());

        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.INPUT_VALIDATION, "IE1");
        ids.addEvent(new Event(user, detectionPoint, detectionSystem));
    }

    private void fireSqlIEvent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User(authentication.getName());

        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.COMMAND_INJECTION, "CIE1");
        ids.addEvent(new Event(user, detectionPoint, detectionSystem));
    }
}
