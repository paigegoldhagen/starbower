package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

/**
 * For preparing and sending a notification.
 */
public class Notifications {
    /**
     * Get a new TrayIcon and add it to the system tray.
     *
     * @param image             the app icon PNG file
     * @return                  the prepared tray icon
     * @throws AWTException     an error occurred with the GUI components or window frame
     */
    public static TrayIcon prepareTrayIcon(BufferedImage image) throws AWTException {
        SystemTray systemTray = SystemTray.getSystemTray();

        TrayIcon trayIcon = new TrayIcon(image, "Astral");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Astral");

        systemTray.add(trayIcon);
        return trayIcon;
    }

    /**
     * Send a notification to the system using the tray icon and the message string.
     *
     * @param trayIcon      the prepared tray icon
     * @param message       the string to display in the notification popup
     */
    public static void sendNotification(TrayIcon trayIcon, String message) {
        trayIcon.displayMessage("Astral", message, TrayIcon.MessageType.INFO);
    }
}
