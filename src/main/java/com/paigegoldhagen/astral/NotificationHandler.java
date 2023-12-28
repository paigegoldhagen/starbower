package com.paigegoldhagen.astral;

import java.awt.*;

/**
 * For sending a notification using a TrayIcon.
 */
public class NotificationHandler {
    /**
     * Display a popup message in the Windows notification area.
     *
     * @param notificationMessage   the message string to display in the notification popup
     * @param trayIcon              the TrayIcon used to send the notification
     */
    public static void sendNotification(Message notificationMessage, TrayIcon trayIcon) {
        trayIcon.displayMessage(notificationMessage.getTopLine(), notificationMessage.getLines(), TrayIcon.MessageType.NONE);
    }
}
