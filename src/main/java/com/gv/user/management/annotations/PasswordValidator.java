package com.gv.user.management.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(final ValidPassword constraintAnnotation) {
        // Initialization code if needed
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }
}
