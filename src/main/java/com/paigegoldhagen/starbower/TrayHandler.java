package com.paigegoldhagen.starbower;

import java.awt.*;

/**
 * Creating a TrayIcon for the Windows system tray.
 */
public class TrayHandler {
    /**
     * Create a TrayIcon using the tray Image and set the image autosize state and tooltip.
     * Add the TrayIcon to the system tray.
     *
     * @param trayImage     the Image to display in the system tray
     *
     * @return              the customised TrayIcon
     * @throws AWTException the TrayIcon could not be added to the system tray
     */
    public static TrayIcon getTrayIcon(Image trayImage) throws AWTException {
        String appName = "Starbower";

        TrayIcon trayIcon = new TrayIcon(trayImage, appName);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip(appName);

        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.add(trayIcon);

        return trayIcon;
    }
}