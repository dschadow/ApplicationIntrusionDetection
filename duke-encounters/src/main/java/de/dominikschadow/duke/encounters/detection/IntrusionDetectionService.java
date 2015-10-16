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
package de.dominikschadow.duke.encounters.detection;

import org.owasp.appsensor.core.AppSensorClient;
import org.owasp.appsensor.core.DetectionSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles all intrusion detection services like loading the configuration.
 *
 * @author Dominik Schadow
 */
@Service
public class IntrusionDetectionService {
    @Autowired
    private AppSensorClient appSensorClient;

    public DetectionSystem getDetectionSystem() {
        return new DetectionSystem(appSensorClient.getConfiguration().getServerConnection()
                .getClientApplicationIdentificationHeaderValue());
    }
}
