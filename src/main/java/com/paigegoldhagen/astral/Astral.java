package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * The entrypoint to the app.
 */
public class Astral {
    /**
     * Get the event data, display the GUI using the default/saved user preference,
     * prepare the tray icon, and send a notification if UTC time matches an event time.
     * Pause for 1 second or 1 minute at the end of every loop.
     *
     * @throws InterruptedException     the current thread was interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        List<Events> eventsList = FileHandler.mapCsvToBeans();
        List<Object[]> eventsTimetable = EventsHandler.getEventsTimetable(eventsList);

        String userPreference = PreferenceHandler.getUserPreference();
        GUI.displayGUI(userPreference);

        TrayIcon trayIcon;
        try {
            BufferedImage appIcon = FileHandler.loadImage();
            trayIcon = Notifications.prepareTrayIcon(appIcon);
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            int notificationBuffer = GUI.getUserChoice();
            PreferenceHandler.setUserPreference(notificationBuffer);
            LocalTime utcTime = TimeHandler.getUtcTime();
            long millisecondsToWait = 1000;

            String message = EventsHandler.setEventMessage(eventsTimetable, notificationBuffer, utcTime);

            if (message != null) {
                Notifications.sendNotification(trayIcon, message);
                millisecondsToWait = 60000;
            }

            Thread.sleep(millisecondsToWait);
        }
    }
}
