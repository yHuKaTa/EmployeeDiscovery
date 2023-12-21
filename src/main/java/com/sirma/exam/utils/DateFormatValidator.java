package com.sirma.exam.utils;

import com.sirma.exam.utils.annotations.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that implements the {@link ConstraintValidator} interface to validate a date format.
 * The class uses a regular expression to match the date format.
 */
public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {
    private static final String regex = RegExTemplate.getRegex();
    private static final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
