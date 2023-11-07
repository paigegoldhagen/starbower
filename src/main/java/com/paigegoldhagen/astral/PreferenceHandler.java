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
     * @return      the integer of the saved user preference
     */
    public static String getUserPreference() {
        return prefs.get("NOTIF_PREF", String.valueOf(10));
    }

    /**
     * Set the user preference in the Windows Registry.
     *
     * @param userChoice    the user choice from the dropdown list
     */
    public static void setUserPreference(int userChoice) {
        prefs.putInt("NOTIF_PREF", userChoice);
    }
}
