package com.paigegoldhagen.astral;

import java.awt.*;

/**
 * For sending a notification using a TrayIcon.
 */
public class Notifications {
    /**
     * Send a notification to the system using the tray icon and the message string.
     *
     * @param trayIcon    the prepared tray icon
     * @param message     the string to display in the notification popup
     */
    public static void sendNotification(TrayIcon trayIcon, String message) {
        trayIcon.displayMessage("Astral", message, TrayIcon.MessageType.INFO);
    }
}
