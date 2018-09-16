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
package de.dominikschadow.dukeencounters.account;

import de.dominikschadow.dukeencounters.Constants;
import de.dominikschadow.dukeencounters.user.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

/**
 * Validates a request to change the users password. Makes sure that the current password is correct and that the new
 * password matches its confirmation.
 *
 * @author Dominik Schadow
 */
@Component
@AllArgsConstructor
public class PasswordChangeValidator implements Validator {
    private final SpringValidatorAdapter validator;
    private final UserService userService;

    @Override
    public boolean supports(final Class<?> clazz) {
        return PasswordChange.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        validator.validate(target, errors);

        PasswordChange passwordChange = (PasswordChange) target;

        // FIXME currentPassword
        if (!userService.confirmPassword("", passwordChange.getCurrentPassword())) {
            errors.rejectValue("currentPassword", Constants.CURRENT_PASSWORD_NOT_CORRECT_ERROR_CODE);
        }

        if (StringUtils.length(passwordChange.getNewPassword()) < 10) {
            errors.rejectValue("newPassword", Constants.UNSAFE_PASSWORD_ERROR_CODE);
        }

        if (!passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConfirmation())) {
            errors.rejectValue("newPassword", Constants.NOT_MATCHING_PASSWORDS_ERROR_CODE);
        }
    }
}
