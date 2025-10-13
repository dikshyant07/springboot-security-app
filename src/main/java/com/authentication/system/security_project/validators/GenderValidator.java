package com.authentication.system.security_project.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = GenderValidatorConstraint.class)
public @interface GenderValidator {
    String message() default "Please provide MALE,FEMALE,OR OTHERS";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
