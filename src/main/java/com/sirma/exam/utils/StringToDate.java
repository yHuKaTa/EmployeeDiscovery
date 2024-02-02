package com.sirma.exam.utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A utility class that converts a string representation of a date to a {@link LocalDate}.
 * The class uses a regular expression to parse the date.
 */
public class StringToDate {
    /**
     * Converts a string representation of the date to a {@link LocalDate}
     * @param date {@link String} of the date to be converted
     * @return {@link LocalDate} The converted date, or null if the input date is invalid
     */
    public static LocalDate toLocalDate(String date) {
        if (Objects.isNull(date) || date.isEmpty()) {
            return null;
        }
        LocalDate generatedDate = null;
        int month = 0;
        List<String> rawData = Arrays.stream(date.trim().split("[\\s\\.\\-\\/,\\\\]")).toList();
        Iterator<String> iterator = rawData.iterator();
        while (iterator.hasNext()) {
            String searched = iterator.next();
            if (searched.equals("г")) {
                iterator.remove();
                break;
            }
        }
        String regex = ReadLabelledMonths.monthsToRegex();
        List<String> searchedMonth = ReadLabelledMonths.monthsToList();
        int index = -1;
        for (String rawDatum : rawData) {
            if (rawDatum.matches(regex)) {
                for (int j = 0; j < searchedMonth.size(); j++) {
                    if (searchedMonth.get(j).equals(rawDatum)) {
                        month = (j % 12) + 1;
                        break;
                    }
                }
                break;
            }
            index++;
        }

        int year = 0;
        int day = 0;
        switch (index) {
            case 0: {
                day = Integer.parseInt(rawData.get(1));
                year = Integer.parseInt(rawData.get(2));

                break;
            }
            case 1: {
                if (Integer.parseInt(rawData.get(0)) > 31) {
                    year = Integer.parseInt(rawData.get(0));
                    day = Integer.parseInt(rawData.get(2));
                } else {
                    day = Integer.parseInt(rawData.get(0));
                    year = Integer.parseInt(rawData.get(2));
                }
                break;
            }
            default: {
                if (Integer.parseInt(rawData.get(0)) > 31) {
                    year = Integer.parseInt(rawData.get(0));
                    month = Integer.parseInt(rawData.get(1));
                    day = Integer.parseInt(rawData.get(2));
                } else {
                    day = Integer.parseInt(rawData.get(0));
                    month = Integer.parseInt(rawData.get(1));
                    year = Integer.parseInt(rawData.get(2));
                }
                break;
            }
        }

        try {
            generatedDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            System.err.println(date + " is not valid date!");
        }
        return generatedDate;
    }
}
