package com.paigegoldhagen.starbower;

import com.formdev.flatlaf.FlatLaf;
import com.paigegoldhagen.starbower.themes.StarbowerDarkLAF;
import com.paigegoldhagen.starbower.themes.StarbowerLightLAF;

import java.util.prefs.Preferences;

/**
 * Managing the custom GUI themes.
 */
public class ThemeHandler {
    /**
     * Register the custom theme resource folder location.
     * Get the default or saved theme preference from the Windows Registry
     * and set the GUI theme based on the preference.
     *
     * @param windowsRegistry   the user preferences for Starbower in the Windows Registry
     */
    public static void initialiseLookAndFeel(Preferences windowsRegistry) {
        FlatLaf.registerCustomDefaultsSource("themes");
        String themePreference = windowsRegistry.get("Theme", "Light");
        setTheme(themePreference);
    }

    /**
     * Determine which theme to use and initialise the theme.
     *
     * @param themePreference   the default or saved theme preference
     */
    public static void setTheme(String themePreference) {
        if (getLightThemeName().contains(themePreference)) {
            StarbowerLightLAF.setup();
        }
        else {
            StarbowerDarkLAF.setup();
        }
    }

    /**
     * Create a new instance of the light look and feel class and get the theme name.
     *
     * @return  the theme name as a string
     */
    private static String getLightThemeName() {
        StarbowerLightLAF lightTheme = new StarbowerLightLAF();
        return lightTheme.getName();
    }
}