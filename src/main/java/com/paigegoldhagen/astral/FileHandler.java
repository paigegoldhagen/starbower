package com.paigegoldhagen.astral;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * For finding and reading resource files.
 */
public class FileHandler {
    /**
     * Find the location of the text file and prepare it for reading using InputStream.
     *
     * @return the input stream of the CSV file
     */
    public static InputStream readCsvFile() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream("event-data.csv");
    }

    /**
     * Find the location of the image file and get it as an InputStream.
     *
     * @return              the input stream of the PNG image
     * @throws IOException  the file couldn't be found or read
     */
    public static BufferedImage loadImage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream("icon.png");

        assert stream != null;
        return ImageIO.read(stream);
    }
}
