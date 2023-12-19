package com.sirma.exam.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A utility class that reads a CSV file and returns month names in different languages and abbreviations.
 */
public class ReadLabelledMonths {
    /**
     * Reads a CSV file and return List containing month names in different languages and abbreviations.
     *
     * @return A {@link List} of month names
     */
    public static List<String> monthsToList() {
        List<String[]> val = ReadFromCsv.read("months.csv");
        List<String> months = new LinkedList<>();
        if (Objects.nonNull(val) && !val.isEmpty()) {
            for (String[] line : val) {
                months.addAll(Arrays.asList(line));
            }
        }
        return months;
    }

    /**
     * Reads a CSV file and return regular expression containing month names in different languages and abbreviations.
     *
     * @return A {@link String} of regular expression for month names
     */
    public static String monthsToRegex() {
        StringBuilder buffer = new StringBuilder();
        List<String[]> val = ReadFromCsv.read("months.csv");
        if (Objects.nonNull(val) && !val.isEmpty()) {
            String last = val.getLast()[val.getLast().length-1];
            for (String[] line : val) {
                if (Objects.nonNull(line)) {
                    for (String month : line) {
                        if (!month.isEmpty() && !last.isEmpty() && last.equals(month)) {
                            buffer.append("(");
                            buffer.append(month);
                            buffer.append(")");
                        } else {
                            buffer.append("(");
                            buffer.append(month);
                            buffer.append(")|");
                        }
                    }
                }
            }
        }
        if (buffer.isEmpty()) {
            return "";
        } else {
            return buffer.toString();
        }
    }
}
