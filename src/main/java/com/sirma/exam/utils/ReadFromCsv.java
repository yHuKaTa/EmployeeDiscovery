package com.sirma.exam.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A utility class that reads a CSV file.
 */
public class ReadFromCsv {
    /**
     * Reads a CSV file and returns a list of strings arrays.
     * @param location {@link String} The path to the CSV file
     * @return {@link List} A list of strings arrays or null if the file could not be read
     */
    public static List<String[]> read(String location) {
        List<String[]> values = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String line;
            while (Objects.nonNull(line = reader.readLine())) {
                values.add(line.trim().split("\s*,\s*"));
            }
        } catch (IOException e) {
            System.err.println("Could not read from " + location);
            System.err.println(e.getMessage());
        }
        return values;
    }
}
