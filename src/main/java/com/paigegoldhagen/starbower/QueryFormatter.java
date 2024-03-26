package com.paigegoldhagen.starbower;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Replacing placeholder text in SQL query strings.
 */
public class QueryFormatter {
    /**
     * Read each line from the text reader and replace any placeholder text.
     *
     * @param folderPath    the URL of a resource folder as a string
     * @param textReader    a class to read from a character input stream
     *
     * @return              the formatted query string
     * @throws IOException  a resource folder/file could not be found or read
     */
    public static String buildQueryString(String folderPath, BufferedReader textReader) throws IOException {
        StringBuilder queryBuilder = new StringBuilder();
        String line = textReader.readLine();

        while (line != null) {
            replacePlaceholderText(folderPath, queryBuilder, line);
            line = textReader.readLine();
        }
        return String.valueOf(queryBuilder);
    }

    /**
     * Replace placeholder text with the URL of a resource folder.
     *
     * @param folderPath    the URL of a resource folder as a string
     * @param queryBuilder  the current query string
     * @param line          the current line of text being read
     */
    private static void replacePlaceholderText(String folderPath, StringBuilder queryBuilder, String line) {
        String filePathPlaceholder = "${CurrentWorkingDirectory}";

        if (line.contains(filePathPlaceholder)) {
            String filePath = line.replace(filePathPlaceholder, folderPath);
            queryBuilder.append(filePath);
        }
        else {
            queryBuilder.append(line);
        }
    }
}