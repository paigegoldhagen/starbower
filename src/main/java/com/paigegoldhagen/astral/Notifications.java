package com.paigegoldhagen.astral;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * For preparing and sending a notification.
 */
public class Notifications {
    private static TrayIcon trayIcon;

    /**
     * Get the app icon from the FileHandler class and add the tray icon to the system tray.
     *
     * @throws AWTException         an error occurred with the GUI components or window frame
     * @throws InterruptedException the current thread was interrupted
     */
    public static void prepareTrayIcon() throws AWTException, InterruptedException {
        Thread currentThread = Thread.currentThread();
        SystemTray systemTray = SystemTray.getSystemTray();

        try {
            Image img = FileHandler.loadImage();
            trayIcon = new TrayIcon(img, "Astral");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Astral");

        if (!currentThread.isInterrupted()) {
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                systemTray.remove(trayIcon);
                currentThread.interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calculate the notification time with [event time - notification buffer in minutes]
     * and pass a message string to the sendNotification method.
     *
     * @return the Runnable method which calculates the notification time and passes a message string if the notification time matches the current time
     */
    public static Runnable prepareNotification() {
        Thread currentThread = Thread.currentThread();
        return () -> {
            LocalTime currentTime = TimeHandler.getCurrentTime();
            int notificationBuffer = GUI.notificationBuffer;

            List<Object[]> eventsTimetable = EventsHandler.eventsTimetable;
            for (Object[] event : eventsTimetable) {
                String bossName = (String) event[0];
                List<LocalTime> projectedEventTimes = (List<LocalTime>) event[1];

                for (LocalTime time : projectedEventTimes) {
                    time = time.minusMinutes(notificationBuffer);

                    if (time.equals(currentTime)) {
                        String message = bossName + " will spawn in " + notificationBuffer + " minutes!";

                        try {
                            Notifications.sendNotification(message);
                            break;
                        } catch (AWTException e) {
                            currentThread.interrupt();
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
    }

    /**
     * Send the notification to the system using the tray icon and the message string passed from the prepareNotification method.
     *
     * @param message               the string to display in the notification popup
     * @throws AWTException         an error occurred with the GUI components or window frame
     * @throws InterruptedException the current thread was interrupted
     */
    private static void sendNotification(String message) throws AWTException {
        trayIcon.displayMessage("Astral", message, TrayIcon.MessageType.INFO);
    }
}