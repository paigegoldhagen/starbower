package com.paigegoldhagen.astral;

import java.util.prefs.Preferences;

/**
 * For saving and accessing user preferences in the Windows Registry.
 */
public class PreferenceHandler {
    public static Preferences prefs = Preferences.userNodeForPackage(PreferenceHandler.class);

    /**
     * Get the default or saved time preference.
     *
     * @return      the time preference string
     */
    public static String getTimePreference() {
        return prefs.get("TIME_PREF", "10");
    }

    /**
     * Save the user choice for the notification reminder.
     *
     * @param timeChoice    the user choice for the notification reminder in minutes
     */
    public static void setTimePreference(String timeChoice) {
        prefs.put("TIME_PREF", timeChoice);
    }

    /**
     * Get the default or saved event notification preferences.
     *
     * @param defaultEventChoices       the default preferences based on the number of events
     * @return                          the event preferences string
     */
    public static String getEventPreferences(String defaultEventChoices) {
        return prefs.get("EVENT_PREF", defaultEventChoices);
    }

    /**
     * Save the user choice for the event reminders.
     *
     * @param eventChoices      the user choices for receiving notifications per event
     */
    public static void setEventPreferences(String eventChoices) {
        prefs.put("EVENT_PREF", eventChoices);
    }
}
