package com.paigegoldhagen.astral;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * For finding and reading resource files and mapping CSV data.
 */
public class FileHandler {
    /**
     * Find the location of the text file and prepare it for reading using InputStream.
     *
     * @return      the input stream of the CSV file
     */
    private static InputStream readCsvFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("event-data.csv");
    }

    /**
     * Map the data from the CSV file to the Events class.
     *
     * @return      the Events class as a list of beans
     */
    public static List<Events> mapCsvToBeans() {
        InputStream eventsData = readCsvFile();

        return new CsvToBeanBuilder<Events>(new InputStreamReader(eventsData))
                .withType(Events.class)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .build()
                .parse();
    }

    /**
     * Find the location of the image file and get it as an InputStream.
     *
     * @return                  the input stream of the PNG image
     * @throws IOException      the file couldn't be found or read
     */
    public static BufferedImage loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream("icon.png");

        assert stream != null;
        return ImageIO.read(stream);
    }
}
