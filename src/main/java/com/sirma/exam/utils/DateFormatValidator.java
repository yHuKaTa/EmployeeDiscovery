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
    private static final String regex = getRegex();
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

    /**
     * <p>Generates the regular expression for the date format.</p>
     * <p>The method uses a StringBuilder to construct the regular expression.</p>
     * <p>The regular expression supports the following date formats:</p>
     *      <li>d-MMMM-yyyy,d-MMM-yyyy,d-MM-yyyy,</li>
     *     <li>dd-MMMM-yyyy,dd-MMM-yyyy,dd-MM-yyyy,</li>
     *     <li>MMMM-d-yyyy,MMM-d-yyyy,</li>
     *     <li>MMMM-dd-yyyy,MMM-dd-yyyy,</li>
     *     <li>yyyy-MMMM-d,yyyy-MMM-d,yyyy-MM-d,</li>
     *     <li>yyyy-MMMM-dd,yyyy-MMM-dd,yyyy-MM-dd</li>
     * <p>The separator character between the date components can be any of the following:</p>
     *      <li>dot</li>
     *      <li>comma</li>
     *      <li>whitespace</li>
     *      <li>backslash</li>
     *      <li>forward slash</li>
     * @return A {@link String} of regular expression for the date format
     */
    private static String getRegex() {
        StringBuilder builder = new StringBuilder();
        String delemiter = "((\\s)|(.)|(-)|(/)|(,)|(\\\\))";

        builder.append("(((3[01])|([12][0-9])|((0)?[1-9]))");
        builder.append(delemiter);
        builder.append("(((1[1,2])|((0)?[1-9]))|(");
        builder.append(ReadLabelledMonths.monthsToRegex() + "))");
        builder.append(delemiter);
        builder.append("(2[0-9]{3})");
        builder.append("(((\\s)?г(.)?)?))|");

        builder.append("(" + ReadLabelledMonths.monthsToRegex() + ")");
        builder.append(delemiter);
        builder.append("((3[01])|([12][0-9])|((0)?[1-9]))");
        builder.append(delemiter);
        builder.append("(2[0-9]{3})");
        builder.append("(((\\s)?г(.)?)?))|");

        builder.append("((2[0-9]{3})");
        builder.append(delemiter);
        builder.append("(((1[1,2])|((0)?[1-9]))|(");
        builder.append(ReadLabelledMonths.monthsToRegex() + "))");
        builder.append(delemiter);
        builder.append("((3[01])|([12][0-9])|((0)?[1-9])))");

        return builder.toString();
    }
}
