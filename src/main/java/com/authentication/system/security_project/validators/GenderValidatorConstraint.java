package com.authentication.system.security_project.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenderValidatorConstraint implements ConstraintValidator<GenderValidator, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<String> genders = List.of("MALE", "FEMALE", "OTHERS");
        return genders.stream().anyMatch(s::equals);
    }
}
