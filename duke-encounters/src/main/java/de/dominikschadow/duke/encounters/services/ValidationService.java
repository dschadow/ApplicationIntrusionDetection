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
package de.dominikschadow.duke.encounters.services;

import com.google.common.base.Strings;
import de.dominikschadow.duke.encounters.domain.DukeEncountersUser;
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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Dominik Schadow
 */
@Service
public class ValidationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationService.class);
    private DetectionSystem detectionSystem;

    @Autowired
    private AppSensorClient appSensorClient;

    @Autowired
    private EventManager ids;

    @PostConstruct
    public void init() {
        detectionSystem = new DetectionSystem(
                appSensorClient.getConfiguration().getServerConnection()
                        .getClientApplicationIdentificationHeaderValue());
    }

    public void validateSearchFilter(SearchFilter filter) {
        if (!Strings.isNullOrEmpty(filter.getEvent())) {
            if (hasXssPayload(filter.getEvent())) {
                reactToXss();
            }
            // TODO AID check for XSS or SQLi
        }

        if (!Strings.isNullOrEmpty(filter.getLocation())) {
            // TODO AID check for XSS or SQLi
        }

        if (!Strings.isNullOrEmpty(filter.getCountry())) {
            // TODO AID check for XSS or SQLi
        }

        if (filter.getYear() > 0) {
            if (filter.getYear() < 1995) {
                LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as year of the event - possible typo",
                        filter.getYear());
                // TODO AID react
            }
        }

        if (!Strings.isNullOrEmpty(filter.getLikelihood())) {
            try {
                Likelihood likelihood = Likelihood.valueOf(filter.getLikelihood());
            } catch (IllegalArgumentException ex) {
                LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured enum " +
                        "range", filter.getLikelihood());
                // TODO AID react
            }
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            LOGGER.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range",
                    filter.getConfirmations());
            // TODO AID react
        }
    }

    public void validateUser(DukeEncountersUser user) {
        // TODO AID created user must have role USER

    }

    public void validateEncounterId(long id) {
        // TOTOD AID validate id
    }

    private boolean hasXssPayload(String payload) {
        return StringUtils.contains(payload, "<script>");
    }

    private void reactToXss() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User(authentication.getName());

        DetectionPoint detectionPoint = new DetectionPoint(DetectionPoint.Category.INPUT_VALIDATION, "IE1");
        ids.addEvent(new Event(user, detectionPoint, detectionSystem));
    }
}
