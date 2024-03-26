package com.paigegoldhagen.starbower;

import java.awt.*;
import java.util.List;

/**
 * Setting and getting Images used in the GUI, Windows taskbar, and system tray.
 */
public class AppImages {
    private final Image TrayImage;
    private final List<Image> AppIconList;

    public AppImages(Image trayImage, List<Image> appIconList) {
        this.TrayImage = trayImage;
        this.AppIconList = appIconList;
    }

    public Image getTrayImage() {return TrayImage;}
    public List<Image> getAppIconList() {return AppIconList;}
}