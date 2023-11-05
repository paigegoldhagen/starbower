package com.paigegoldhagen.astral;

import java.awt.*;

/**
 * The entrypoint to the app.
 */
public class Astral {
    /**
     * Initialise the event times, GUI, and tray icon,
     * and schedule a task from the TimeHandler class.
     */
    public static void main(String[] args) {
        EventsHandler.getProjectedEventTimes();
        GUI.displayGUI();
        TimeHandler.scheduleTaskEverySecond();

        try {
            Notifications.prepareTrayIcon();
        } catch (AWTException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
