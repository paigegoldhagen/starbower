package com.paigegoldhagen.astral;

import java.awt.*;
import java.util.List;

/**
 * For setting and getting images used in the app, Windows taskbar, and system tray.
 */
public class AppImages {
    private final Image TrayImage;
    private final List<Image> AppIcons;

    public AppImages(Image trayImage, List<Image> appIcons) {
        this.TrayImage = trayImage;
        this.AppIcons = appIcons;
    }

    public Image getTrayImage() {return TrayImage;}
    public List<Image> getAppIcons() {return AppIcons;}
}
