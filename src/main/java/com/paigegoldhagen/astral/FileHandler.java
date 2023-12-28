package com.paigegoldhagen.astral;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * For finding and reading resource files.
 */
public class FileHandler {
    /**
     * Find the location of a resource file and prepare it for reading using InputStream.
     *
     * @param file      the folder, file name, and file extension of the target file
     * @return          the InputStream of the file
     */
    private static InputStream getInputStream(String file) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(file);
    }

    /**
     * Map event data from the CSV InputStream into a list of Event class instances.
     *
     * @return      a list of new Event instances with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     */
    public static List<Event> mapEventData() {
        InputStream stream = getInputStream("data/event-data.csv");

        return new CsvToBeanBuilder<Event>(new InputStreamReader(stream))
                .withType(Event.class)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .build()
                .parse();
    }

    /**
     * Map festival data from the CSV InputStream into a list of Festival class instances.
     *
     * @return      a list of new Festival instances with a name, start date, and end date
     */
    public static List<Festival> mapFestivalData() {
        InputStream stream = getInputStream("data/festival-data.csv");

        return new CsvToBeanBuilder<Festival>(new InputStreamReader(stream))
                .withType(Festival.class)
                .build()
                .parse();
    }

    /**
     * Load an Image from an image resource file.
     *
     * @param file              the folder, file name, and file extension of the target file
     * @return                  a new Image
     * @throws IOException      the file couldn't be found or read
     */
    public static Image loadImage(String file) throws IOException {
        InputStream stream = getInputStream("images/" + file);
        return ImageIO.read(stream);
    }

    /**
     * Load Images from an array of image resource files and add them to a list.
     *
     * @param imageFiles        an array of target files
     * @return                  a list of new Images
     * @throws IOException      a file couldn't be found or read
     */
    public static List<Image> loadImageList(String[] imageFiles) throws IOException {
        List<Image> imageList = new ArrayList<>();

        for (String file : imageFiles) {
            InputStream stream = getInputStream("images/" + file);
            imageList.add(ImageIO.read(stream));
        }
        return imageList;
    }
}
