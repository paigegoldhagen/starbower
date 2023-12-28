package com.paigegoldhagen.astral;

import java.awt.*;

/**
 * For initialising a TrayIcon used in the Windows system tray.
 */
public class TrayHandler {
    /**
     * Create a TrayIcon using an Image and set the TrayIcon visual behaviour.
     * Get the SystemTray instance and add the TrayIcon.
     *
     * @param trayImage         an Image file
     * @return                  the new TrayIcon added to the system tray
     * @throws AWTException     an error occurred with the GUI components or window
     */
    public static TrayIcon getTrayIcon(Image trayImage) throws AWTException {
        TrayIcon trayIcon = new TrayIcon(trayImage, "Astral");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Astral");

        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.add(trayIcon);
        return trayIcon;
    }
}
