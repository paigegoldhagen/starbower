package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * For preparing the tray icon.
 */
public class TrayHandler {
    /**
     * Get a new TrayIcon and add it to the system tray.
     *
     * @param image             the app icon BufferedImage
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
}
