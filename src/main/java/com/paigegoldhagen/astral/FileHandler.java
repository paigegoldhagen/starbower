package com.paigegoldhagen.astral;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * For finding and preparing resource files.
 */
public class FileHandler {
    /**
     * Find the location of a file and prepare it for reading using InputStream.
     *
     * @param fileName       the name and extension of the target file
     * @return              the input stream of the file
     */
    public static InputStream getInputStream(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    /**
     * Load a BufferedImage from an image file.
     *
     * @param fileName           the name and extension of the target file
     * @return                  the BufferedImage from reading the file
     * @throws IOException      the file couldn't be found or read
     */
    public static BufferedImage loadImage(String fileName) throws IOException {
        InputStream stream = getInputStream(fileName);
        assert stream != null;
        return ImageIO.read(stream);
    }
}
