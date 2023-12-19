package com.sirma.exam.utils.annotations;

import com.sirma.exam.utils.DateFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A custom annotation that implements the {@link jakarta.validation.ConstraintValidator} interface to validate a date format.
 */
@Target({ElementType.FIELD,ElementType.LOCAL_VARIABLE,ElementType.PARAMETER})
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DateFormatValidator.class})
@Documented
public @interface ValidDateFormat {
    String message() default "Invalid date format! View ISO 8601 for valid date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
