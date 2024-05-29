package com.paigegoldhagen.starbower;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loading resource files, creating loaded file classes and registering custom resources.
 */
public class ResourceHandler {
    /**
     * Read from the QueryFiles CSV to get the query file names
     * and get the query string using the file name.
     * Add query name/string pairs to a list.
     *
     * @return              a Queries class with a list of query name/string pairs
     * @throws IOException  a resource folder/file could not be found or read
     */
    public static Queries getDatabaseQueries() throws IOException {
        List<Pair<String, String>> queryList = new ArrayList<>();

        BufferedReader textReader = FileHandler.getTextReader("init/QueryFiles.csv");
        String line = textReader.readLine();

        while (line != null) {
            String fileName = line;
            String queryName = fileName.substring(0, fileName.lastIndexOf("."));
            String queryString = getQueryString(fileName);

            queryList.add(new MutablePair<>(queryName, queryString));

            line = textReader.readLine();
        }
        return new Queries(queryList);
    }

    /**
     * Get the URL of the data folder as a string and build a query string
     * using the URL and a text reader of the query file.
     *
     * @param fileName  a query file name
     *
     * @return              a formatted query string
     * @throws IOException  a resource folder/file could not be found or read
     */
    private static String getQueryString(String fileName) throws IOException {
        BufferedReader textReader = FileHandler.getTextReader("queries/" + fileName);
        String dataFolderPath = String.valueOf(FileHandler.getResourceFolderURL("data"));
        return QueryFormatter.buildQueryString(dataFolderPath, textReader);
    }

    /**
     * Load the Images from the FileHandler class and set the trayImage Image.
     * Create a new AppImages class.
     *
     * @return              an AppImages class with a tray Image and an Image list
     * @throws IOException  a resource folder/file could not be found or read
     */
    public static AppImages getAppImages() throws IOException {
        List<Image> imageList = FileHandler.loadImageList();
        Image trayImage = null;

        for (Image image : imageList) {
            if (image.getWidth(null) == 24) {
                trayImage = image;
            }
        }
        return new AppImages(trayImage, imageList);
    }

    /**
     * Read from the FontFiles CSV to get the font file names.
     * Get the input stream for each font and register the font in the graphics environment.
     *
     * @throws IOException          a resource folder/file could not be found or read
     * @throws FontFormatException  the font format type is incompatible
     */
    public static void registerCustomFonts() throws IOException, FontFormatException {
        BufferedReader textReader = FileHandler.getTextReader("init/FontFiles.csv");
        String line = textReader.readLine();

        while (line != null) {
            InputStream stream = FileHandler.getInputStream("fonts/" + line);

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, stream);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);

            line = textReader.readLine();
        }
    }

    /**
     * Get the input stream from the DropdownData CSV and map the data to the Dropdown class.
     *
     * @return  a list of Dropdown classes
     */
    public static List<Dropdown> getDropdownList() {
        InputStream dropdownData = FileHandler.getInputStream("init/DropdownData.csv");

        return new CsvToBeanBuilder<Dropdown>(new InputStreamReader(dropdownData))
                .withType(Dropdown.class)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .build()
                .parse();
    }
}