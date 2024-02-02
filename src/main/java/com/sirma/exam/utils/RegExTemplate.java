package com.sirma.exam.utils;

import java.util.Objects;

/**
 * A template utility class for generating month names
 */
public class RegExTemplate {
    private static StringBuilder builder;
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
    public static String getRegex() {
        if (Objects.isNull(builder) || builder.isEmpty()) {
            builder = new StringBuilder();
            String delimiter = "((\\s)|(.)|(-)|(/)|(,)|(\\\\))";

            builder.append("(((3[01])|([12][0-9])|((0)?[1-9]))");
            builder.append(delimiter);
            builder.append("(((1[1,2])|((0)?[1-9]))|(");
            builder.append(ReadLabelledMonths.monthsToRegex());
            builder.append("))");
            builder.append(delimiter);
            builder.append("(2[0-9]{3})");
            builder.append("(((\\s)?г(.)?)?))|(");
            builder.append(ReadLabelledMonths.monthsToRegex());
            builder.append(")");
            builder.append(delimiter);
            builder.append("((3[01])|([12][0-9])|((0)?[1-9]))");
            builder.append(delimiter);
            builder.append("(2[0-9]{3})");
            builder.append("(((\\s)?г(.)?)?)|");

            builder.append("((2[0-9]{3})");
            builder.append(delimiter);
            builder.append("(((1[1,2])|((0)?[1-9]))|(");
            builder.append(ReadLabelledMonths.monthsToRegex());
            builder.append("))");
            builder.append(delimiter);
            builder.append("((3[01])|([12][0-9])|((0)?[1-9])))");

            return builder.toString();
        } else return builder.toString();
    }
}
