package com.paigegoldhagen.astral;

import java.util.prefs.Preferences;

/**
 * For saving and accessing user preferences in the Windows Registry.
 */
public class PreferenceHandler {
    public static Preferences prefs = Preferences.userNodeForPackage(PreferenceHandler.class);

    /**
     * Get the saved user preference from the Windows Registry.
     *
     * @return the string of the saved user preference
     */
    public static String getUserPreference() {
        int savedPreference = prefs.getInt("NOTIF_PREF", 10);
        return String.valueOf(savedPreference);
    }

    /**
     * Set the user preference in the Windows Registry.
     *
     * @param notificationBuffer the user choice from the dropdown list
     */
    public static void setUserPreference(int notificationBuffer) {
        prefs.putInt("NOTIF_PREF", notificationBuffer);
    }
}
