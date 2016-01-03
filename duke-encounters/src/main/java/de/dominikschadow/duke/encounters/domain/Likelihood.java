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
package de.dominikschadow.duke.encounters.domain;

import com.google.common.base.Strings;

import java.util.List;
import java.util.Objects;

/**
 * Encounter likelihood enum.
 *
 * @author Dominik Schadow
 */
public enum Likelihood {
    ALL("*"), NOT_CONFIRMED("not confirmed"), PLAUSIBLE("plausible"), CONFIRMED("confirmed");

    private final String name;

    Likelihood(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Likelihood fromString(String value) {
        if (!Strings.isNullOrEmpty(value)) {
            for (Likelihood l : Likelihood.values()) {
                if (value.equalsIgnoreCase(l.getName())) {
                    return l;
                }
            }
        }

        throw new IllegalArgumentException("No enum found for " + value);
    }

    public static Likelihood getLikelihood(List<Confirmation> confirmations) {
        if (Objects.isNull(confirmations) || confirmations.isEmpty()) {
            return Likelihood.NOT_CONFIRMED;
        }

        return confirmations.size() < 3 ? Likelihood.PLAUSIBLE : Likelihood.CONFIRMED;
    }
}
