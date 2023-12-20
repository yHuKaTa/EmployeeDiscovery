package com.sirma.exam.utils;

import java.time.LocalDate;

/**
 * A utility class for validating dates.
 */
public class DateValidator {

    /**
     * Checks if two dates are equal.
     * @param searchedDate The {@link LocalDate} to search for
     * @param currentDate The current {@link LocalDate}
     * @return true if dates are equal, false otherwise
     */
    public static boolean isDatesEquals(LocalDate searchedDate, LocalDate currentDate) {
        return searchedDate.isEqual(currentDate);
    }

    /**
     * Checks if a date range is valid
     * @param startDate The start {@link LocalDate}
     * @param endDate The end {@link LocalDate}
     * @return true if the date range is valid, false otherwise
     */
    public static boolean isDatesValid(LocalDate startDate, LocalDate endDate) {
        return (startDate.isEqual(LocalDate.now()) || startDate.isAfter(LocalDate.now())) &&
                (endDate.isEqual(LocalDate.now()) || endDate.isAfter(LocalDate.now())) &&
                (startDate.isBefore(endDate) || startDate.isEqual(endDate)) &&
                (endDate.isAfter(startDate) || endDate.isEqual(startDate));
    }

    /**
     * Checks if a searched date is within a date range
     * @param searchedDate The {@link LocalDate} to search for
     * @param startDate The start {@link LocalDate}
     * @param endDate The end {@link LocalDate}
     * @return true if the searched date is within the date range, false otherwise
     */
    public static boolean isSearchedDateValid(LocalDate searchedDate, LocalDate startDate, LocalDate endDate) {
        return (searchedDate.isEqual(startDate) || searchedDate.isAfter(startDate)) &&
                (searchedDate.isEqual(endDate) || searchedDate.isBefore(endDate));
    }

    /**
     * Checks if an edited end date is equal or after current end date
     * @param editedEndDate The edited end {@link LocalDate}
     * @param startDate The start {@link LocalDate}
     * @param endDate The end {@link LocalDate}
     * @return true if the edited end date is valid, false otherwise
     */
    public static boolean isEditedEndDateValid(LocalDate editedEndDate, LocalDate startDate, LocalDate endDate) {
        return (editedEndDate.isEqual(endDate) || editedEndDate.isAfter(endDate)) &&
                (editedEndDate.isAfter(startDate) || editedEndDate.isEqual(startDate));
    }
}
