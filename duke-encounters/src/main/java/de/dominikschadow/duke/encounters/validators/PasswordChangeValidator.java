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

import de.dominikschadow.duke.encounters.Constants;
import de.dominikschadow.duke.encounters.domain.PasswordChange;
import de.dominikschadow.duke.encounters.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.inject.Named;

/**
 * Validates a request to change the users password. Makes sure that the current password is correct and that the new
 * password matches its confirmation.
 *
 * @author Dominik Schadow
 */
@Named
public class PasswordChangeValidator implements Validator {
    @Autowired
    private SpringValidatorAdapter validator;
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PasswordChange.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);

        PasswordChange passwordChange = (PasswordChange) target;

        if (!userService.confirmPassword(passwordChange.getCurrentPassword())) {
            errors.rejectValue("newPassword", Constants.PASSWORD_ERROR_CODE, Constants
                    .NOT_CORRECT_PASSWORD_ERROR_MESSAGE);
        }

        if (StringUtils.length(passwordChange.getNewPassword()) < 10) {
            errors.rejectValue("newPassword", Constants.PASSWORD_ERROR_CODE, Constants.UNSAFE_PASSWORD_ERROR_MESSAGE);
        }

        if (!passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConfirmation())) {
            errors.rejectValue("newPassword", Constants.PASSWORD_ERROR_CODE, Constants.PASSWORDS_DONT_MATCH_ERROR_MESSAGE);
        }
    }
}
