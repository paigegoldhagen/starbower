package com.paigegoldhagen.starbower;

import java.io.*;
import java.net.URL;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.List;

/**
 * Finding and reading resource files.
 */
public class FileHandler {
    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

    /**
     * Get the input stream of a file and return a BufferedReader class of the input stream.
     *
     * @param filePath  the path of the resource file
     * @return          a class to read from a character input stream
     */
    public static BufferedReader getTextReader(String filePath) {
        InputStream stream = getInputStream(filePath);
        return new BufferedReader(new InputStreamReader(stream));
    }

    /**
     * Get the input stream of a file using the current thread class loader.
     *
     * @param filePath  the path of the resource file
     * @return          an input stream of the file
     */
    public static InputStream getInputStream(String filePath) {
        return CLASS_LOADER.getResourceAsStream(filePath);
    }

    /**
     * Get the URL of a resource folder using the current thread class loader.
     *
     * @param folderName    the name of the resource folder
     * @return              a URL of the resource folder
     */
    public static URL getResourceFolderURL(String folderName) {
        return CLASS_LOADER.getResource(folderName);
    }

    /**
     * Read from the ImageFiles CSV to get the image file names and get the input stream for each file.
     * Add the image input streams to a list.
     *
     * @return              a list of populated Image classes
     * @throws IOException  a resource folder/file could not be found or read
     */
    public static List<Image> loadImageList() throws IOException {
        List<Image> imageList = new ArrayList<>();

        BufferedReader textReader = getTextReader("init/ImageFiles.csv");
        String line = textReader.readLine();

        while (line != null) {
            String fileName = line;
            InputStream stream = getInputStream("images/" + fileName);
            imageList.add(ImageIO.read(stream));
            line = textReader.readLine();
        }
        return imageList;
    }
}