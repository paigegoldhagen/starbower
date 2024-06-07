package com.paigegoldhagen.starbower;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 * Updating the Schedule table, getting upcoming DynamicEvents
 * and sending notifications using a repeating Runnable interface.
 */
public class Notifications {
    /**
     * Schedule a Runnable to retrieve an upcoming DynamicEvents list every second
     * and send a notification if the list is not empty.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param trayIcon              the tray icon in the system tray
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     */
    public static void scheduleNotificationSender(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, TrayIcon trayIcon, List<Dropdown> dropdownList) {
        Runnable sendNotification = sendNotification(databaseConnection, sqlQueries, windowsRegistry, trayIcon, dropdownList);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(sendNotification, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Get the current date in UTC, update the Schedule table, and get the notification reminder time.
     * Get a list of upcoming DynamicEvents using the current date and notification reminder time.
     * If the list is not empty, get the notification message and display a notification using the tray icon.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param trayIcon              the tray icon in the system tray
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     *
     * @return                      the methods to run
     */
    private static Runnable sendNotification(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, TrayIcon trayIcon, List<Dropdown> dropdownList) {
        return () -> {
            try {
                LocalDateTime utcDate = getUtcDate();

                ScheduleHandler.updateScheduleTable(databaseConnection, sqlQueries, utcDate);
                int notifyMinutes = Integer.parseInt(windowsRegistry.get(dropdownList.getFirst().getPreferenceKey(), dropdownList.getFirst().getPreferenceValue()));

                List<DynamicEvent> upcomingDynamicEventList = UpcomingEvents.getUpcomingDynamicEventList(databaseConnection, sqlQueries, utcDate, notifyMinutes);

                if (!upcomingDynamicEventList.isEmpty()) {
                    Message notificationMessage = MessageHandler.getNotificationMessage(upcomingDynamicEventList, notifyMinutes);
                    trayIcon.displayMessage(notificationMessage.getCaption(), notificationMessage.getText(), TrayIcon.MessageType.NONE);
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Get the zone ID of UTC and return the current date of the zone ID.
     *
     * @return  the current date in UTC
     */
    private static LocalDateTime getUtcDate() {
        ZoneId utcZoneID = ZoneId.of("UTC");
        return LocalDateTime.now(utcZoneID).truncatedTo(ChronoUnit.SECONDS);
    }
}