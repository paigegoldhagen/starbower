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
     * Get the SQL queries from the ResourceHandler and get the user preferences from the Windows Registry.
     * Get the database connection and populate the database using the DatabaseHandler.
     * Get the AppImages and Dropdown list for the GUI and sending notifications.
     * Initialise the GUI and prepare notifications.
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     * @throws IOException          a resource folder/file could not be found or read
     * @throws AWTException         the TrayIcon could not be added to the system tray
     * @throws FontFormatException  the font format type is incompatible
     */
    public static void main(String[] args) throws SQLException, IOException, AWTException, FontFormatException {
        Queries sqlQueries = ResourceHandler.getDatabaseQueries();
        Preferences windowsRegistry = Preferences.userNodeForPackage(Starbower.class);

        Connection databaseConnection = DatabaseHandler.getDatabaseConnection();
        DatabaseHandler.populateDatabase(databaseConnection, sqlQueries, windowsRegistry);

        AppImages appImages = ResourceHandler.getAppImages();
        List<Dropdown> dropdownList = ResourceHandler.getDropdownList();

        initialiseGUI(databaseConnection, sqlQueries, windowsRegistry, appImages.getAppIconList(), dropdownList);
        prepareNotifications(databaseConnection, sqlQueries, windowsRegistry, appImages.getTrayImage(), dropdownList);
    }

    /**
     * Register the FlatLaf custom fonts using the ResourceHandler and display the GUI.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the GUI
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     *
     * @throws IOException          a resource folder/file could not be found or read
     * @throws FontFormatException  the font format type is incompatible
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void initialiseGUI(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList, List<Dropdown> dropdownList) throws IOException, FontFormatException, SQLException {
        ResourceHandler.registerCustomFonts();
        GUI.displayGUI(databaseConnection, sqlQueries, windowsRegistry, appIconList, dropdownList);
    }

    /**
     * Get the tray icon added to the system tray and schedule the notification sender.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param trayImage             the Image for creating the TrayImage
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     *
     * @throws AWTException         the TrayIcon could not be added to the system tray
     */
    private static void prepareNotifications(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, Image trayImage, List<Dropdown> dropdownList) throws AWTException {
        TrayIcon trayIcon = TrayHandler.getTrayIcon(trayImage);
        Notifications.scheduleNotificationSender(databaseConnection, sqlQueries, windowsRegistry, trayIcon, dropdownList);
    }
}