package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * For getting unique event groups/subgroups and specialty subgroups.
 */
public class GroupHandler {
    /**
     * Get the event groups from the event list and add each unique group to a list.
     *
     * @param eventList     a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @return              a list of unique event group names
     */
    public static List<String> getUniqueEventGroups(List<Event> eventList) {
        List<String> uniqueEventGroups = new ArrayList<>();

        for (Event event : eventList) {
            String eventGroup = event.getGroup();

            if (!uniqueEventGroups.contains(eventGroup)) {
                uniqueEventGroups.add(eventGroup);
            }
        }

        return uniqueEventGroups;
    }

    /**
     * Get the event subgroups from the event list and add each unique subgroup to a list depending on the event group.
     *
     * @param eventList         a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param lastEventGroup    the last event group name
     *
     * @return                  a list of unique event subgroup names
     */
    public static List<String> getUniqueEventSubgroups(List<Event> eventList, String lastEventGroup) {
        List<String> uniqueEventSubgroups = new ArrayList<>();

        for (Event event : eventList) {
            String eventSubgroup = event.getSubgroup();

            if (!uniqueEventSubgroups.contains(eventSubgroup) && !event.getGroup().equals(lastEventGroup)) {
                uniqueEventSubgroups.add(eventSubgroup);
            }
        }

        return uniqueEventSubgroups;
    }

    /**
     * Determine which subgroups need to be handled differently based on the event group and the subgroup having multiple events sharing the same name.
     * Add the specialty subgroups to a list.
     *
     * @param eventList             a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param uniqueEventGroups     a list of unique event group names
     *
     * @return                      a list of specialty subgroup names
     */
    public static List<String> getSpecialtySubgroupList(List<Event> eventList, List<String> uniqueEventGroups) {
        String firstGroupSpecialtySubgroup = getModeEventSubgroup(eventList, uniqueEventGroups.getFirst());
        String lastGroupSpecialtySubgroup = getModeEventSubgroup(eventList, uniqueEventGroups.getLast());

        return new ArrayList<>(List.of(firstGroupSpecialtySubgroup, lastGroupSpecialtySubgroup));
    }

    /**
     * Get the event subgroups and names corresponding to a specific event group and add them to a list.
     * Determine the mode event pair and get the subgroup name from the pair.
     *
     * @param eventList     a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param eventGroup    the unique event group name
     *
     * @return              the subgroup name string from calculating the mode event name
     */
    private static String getModeEventSubgroup(List<Event> eventList, String eventGroup) {
        List<Pair<String, String>> groupEventsList = new ArrayList<>();

        for (Event event : eventList) {
            if (event.getGroup().equals(eventGroup)) {
                groupEventsList.add(new MutablePair<>(event.getSubgroup(), event.getName()));
            }
        }

        return calculateModeEvent(groupEventsList).getLeft();
    }

    /**
     * Calculate the most frequently occurring (mode) event in the group events list.
     *
     * @param groupEventsList   a list of event subgroup/name pairs
     * @return                  the event subgroup/name pair of the mode event
     */
    private static Pair<String, String> calculateModeEvent(List<Pair<String, String>> groupEventsList) {
        Pair<String, String> modeEvent = null;
        int mostEventOccurrences = 0;

        int groupEventsListSize = groupEventsList.size();

        for (Pair<String, String> event : groupEventsList) {
            int eventOccurrence = 0;

            for (int i = 0; i < groupEventsListSize; i++) {
                if (event.equals(groupEventsList.get(i))) {
                    eventOccurrence++;
                }
            }

            if (eventOccurrence > mostEventOccurrences) {
                mostEventOccurrences = eventOccurrence;
                modeEvent = event;
            }
        }

        return modeEvent;
    }
}
