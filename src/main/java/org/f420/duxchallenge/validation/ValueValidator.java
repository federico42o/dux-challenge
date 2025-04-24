package org.f420.duxchallenge.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

import static org.f420.duxchallenge.utils.ValidationUtils.hasValidValue;

public class ValueValidator implements ConstraintValidator<NotEmpty, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.isNull(s) || !s.trim().isEmpty();
    }
}
