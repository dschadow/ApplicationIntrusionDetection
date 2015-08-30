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
import de.dominikschadow.duke.encounters.domain.Likelihood;
import de.dominikschadow.duke.encounters.domain.SearchFilter;
import de.dominikschadow.duke.encounters.domain.User;
import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.security.logging.SecurityMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Dominik Schadow
 */
@Service
public class ValidationService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private AppSensorClient appSensorClient = new AppSensorClient();

    public void validateSearchFilter(SearchFilter filter) {
        if (!Strings.isNullOrEmpty(filter.getEvent())) {
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
                log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as year of the event - possible typo", filter.getYear());
                // TODO AID react
            }
        }

        if (!Strings.isNullOrEmpty(filter.getLikelihood())) {
            try {
                Likelihood likelihood = Likelihood.valueOf(filter.getLikelihood());
            } catch (IllegalArgumentException ex) {
                log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} as likelihood - out of configured enum range", filter.getLikelihood());
                // TODO AID react
            }
        }

        if (filter.getConfirmations() < 0 || filter.getConfirmations() > 10) {
            log.info(SecurityMarkers.SECURITY_FAILURE, "Requested {} confirmations - out of configured range", filter.getConfirmations());
            // TODO AID react
        }
    }

    public void validateUser(User user) {
        // TODO AID created user must have role USER

    }
}
