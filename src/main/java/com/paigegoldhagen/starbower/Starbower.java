package com.paigegoldhagen.starbower;

import java.util.List;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.prefs.Preferences;

/**
 * Entry point to the app.
 */
public class Starbower {
    /**
     * Get the database connection, Queries class, and the user preferences from the Windows Registry.
     * Populate the database using the database connection, Queries class, and user preferences.
     * Get the AppImages for the GUI and for sending a notification.
     * Initialise the GUI and prepare notifications.
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     * @throws IOException          a resource folder/file could not be found or read
     * @throws AWTException         the TrayIcon could not be added to the system tray
     * @throws FontFormatException  the font format type is incompatible
     */
    public static void main(String[] args) throws SQLException, IOException, AWTException, FontFormatException {
        Connection databaseConnection = DatabaseHandler.getDatabaseConnection();
        Queries sqlQueries = ResourceHandler.getDatabaseQueries();
        Preferences windowsRegistry = Preferences.userNodeForPackage(Starbower.class);

        DatabaseHandler.populateDatabase(databaseConnection, sqlQueries, windowsRegistry);

        AppImages appImages = ResourceHandler.getAppImages();
        initialiseGUI(databaseConnection, sqlQueries, windowsRegistry, appImages.getAppIconList());
        prepareNotifications(databaseConnection, sqlQueries, windowsRegistry, appImages.getTrayImage());
    }

    /**
     * Register the FlatLaf custom fonts and get the Dropdown list using the ResourceHandler
     * and display the GUI.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the GUI
     *
     * @throws IOException          a resource folder/file could not be found or read
     * @throws FontFormatException  the font format type is incompatible
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void initialiseGUI(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList) throws IOException, FontFormatException, SQLException {
        ResourceHandler.registerCustomFonts();
        List<Dropdown> dropdownList = ResourceHandler.getDropdownList();
        GUI.displayGUI(databaseConnection, sqlQueries, windowsRegistry, appIconList, dropdownList);
    }

    /**
     * Get the tray icon added to the system tray and schedule the notification sender.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param trayImage             the Image for creating the TrayImage
     *
     * @throws AWTException         the TrayIcon could not be added to the system tray
     */
    private static void prepareNotifications(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, Image trayImage) throws AWTException {
        TrayIcon trayIcon = TrayHandler.getTrayIcon(trayImage);
        Notifications.scheduleNotificationSender(databaseConnection, sqlQueries, windowsRegistry, trayIcon);
    }
}