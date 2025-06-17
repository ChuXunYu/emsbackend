package com.dne.ems.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // (?=.*[0-9]) a digit must occur at least once
    // (?=.*[a-z]) a lower case letter must occur at least once
    // (?=.*[A-Z]) an upper case letter must occur at least once
    // (?=.*[!@#$%^&*()]) a special character must occur at least once
    // (?=\\S+$) no whitespace allowed in the entire string
    // .{8,} at least 8 characters
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";

    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        return PATTERN.matcher(password).matches();
    }
} 