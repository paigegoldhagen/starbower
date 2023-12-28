package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

/**
 * For setting and getting user preferences for the notification reminder and reminders per event.
 */
public class UserPreferences {
    private String NotifyMinutes;
    private List<Pair<String, Boolean>> NotifyStates;

    public UserPreferences(String notifyMinutes, List<Pair<String, Boolean>> notifyStates) {
        this.NotifyMinutes = notifyMinutes;
        this.NotifyStates = notifyStates;
    }

    public void setNotifyMinutes(String notifyMinutes) {this.NotifyMinutes = notifyMinutes;}
    public String getNotifyMinutes() {return NotifyMinutes;}

    public void setNotifyStates(List<Pair<String, Boolean>> notifyStates) {this.NotifyStates = notifyStates;}
    public List<Pair<String, Boolean>> getNotifyStates() {return NotifyStates;}
}
