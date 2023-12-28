package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * For getting user preferences saved to the Windows Registry and dynamically assigning default or saved notify states.
 */
public class PreferenceHandler {
    /**
     * Get access to the Windows Registry using the Preferences class.
     * Create a new UserPreferences class instance using the default or saved notification minutes and notify states.
     *
     * @param eventList                 a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     * @param specialtySubgroupList     a list of specialty subgroup names
     *
     * @return                          a new UserPreferences instance with the notification minutes and a list of notify states
     */
    public static UserPreferences getUserPreferences(List<Event> eventList, List<String> uniqueEventSubgroups, List<String> specialtySubgroupList) {
        Preferences windowsRegistry = Preferences.userNodeForPackage(Astral.class);

        String notifyMinutes = windowsRegistry.get("Notification Minutes", "10");
        List<Pair<String, Boolean>> notifyStates = getNotifyStates(eventList, uniqueEventSubgroups, specialtySubgroupList, windowsRegistry);

        return new UserPreferences(notifyMinutes, notifyStates);
    }

    /**
     * Get the default notify states using the unique event subgroups and specialty subgroups.
     * Add the default notify states to a list.
     *
     * @param eventList                 a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param windowsRegistry           the path to the Windows Registry data for the app
     *
     * @return                          a list of notify states with an event name and a boolean state
     */
    private static List<Pair<String, Boolean>> getNotifyStates(List<Event> eventList, List<String> uniqueEventSubgroups, List<String> specialtySubgroupList, Preferences windowsRegistry) {
        List<Pair<String, Boolean>> defaultNotifyStates = getDefaultNotifyStates(eventList, uniqueEventSubgroups, specialtySubgroupList);
        List<Pair<String, Boolean>> notifyStates = new ArrayList<>();

        for (Pair<String, Boolean> state : defaultNotifyStates) {
            String eventName = state.getKey();
            boolean isStateEnabled = windowsRegistry.getBoolean(eventName, state.getValue());
            notifyStates.add(new MutablePair<>(eventName, isStateEnabled));
        }

        return notifyStates;
    }

    /**
     * Adjust the formatting of event names based on the specialty subgroups
     * and determine the boolean state of each event based on the unique event subgroups.
     * Add each event name and boolean pair to a list.
     *
     * @param eventList                 a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     * @param specialtySubgroupList     a list of specialty subgroup names
     *
     * @return                          a list of notify states with an event name and a boolean state
     */
    private static List<Pair<String, Boolean>> getDefaultNotifyStates(List<Event> eventList, List<String> uniqueEventSubgroups, List<String> specialtySubgroupList) {
        List<Pair<String, Boolean>> defaultNotifyStates = new ArrayList<>();

        for (Event event : eventList) {
            String eventName = event.getName();
            String eventSubgroup = event.getSubgroup();

            if (specialtySubgroupList.contains(eventSubgroup)) {
                eventName = eventName + " (" + event.getLocation() + ")";
            }

            boolean isEventActive = uniqueEventSubgroups.contains(eventSubgroup);
            defaultNotifyStates.add(new MutablePair<>(eventName, isEventActive));
        }

        for (String subgroup : uniqueEventSubgroups) {
            defaultNotifyStates.add(new MutablePair<>(subgroup, true));
        }

        return defaultNotifyStates;
    }
}
