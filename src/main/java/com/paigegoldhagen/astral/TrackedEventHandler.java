package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * For creating a list of tracked events based on the current time in UTC.
 */
public class TrackedEventHandler {
    /**
     * Get projected event times from an EventTimetable list.
     * Create a new TrackedEvent class instance if an event time matches the current time in UTC.
     * Add the TrackedEvent instances to a list.
     *
     * @param eventList     a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param userPrefs     the user preferences with notification minutes and a list of notify states
     *
     * @return              a list of TrackedEvent instances with type and name strings
     */
    public static List<TrackedEvent> getTrackedEvents(List<Event> eventList, UserPreferences userPrefs) {
        List<EventTimetable> eventTimetables = getEventTimetables(eventList);
        int notifyMinutes = Integer.parseInt(userPrefs.getNotifyMinutes());

        List<TrackedEvent> trackedEvents = new ArrayList<>();

        for (EventTimetable event : eventTimetables) {
            for (LocalTime eventTime : event.getTimes()) {
                eventTime = eventTime.minusMinutes(notifyMinutes);

                if (eventTime.equals(getUtcTime())) {
                    TrackedEvent newEvent = getNewTrackedEvent(event, userPrefs.getNotifyStates());

                    if (newEvent != null) {
                        trackedEvents.add(newEvent);
                    }
                }
            }
        }

        return trackedEvents;
    }

    /**
     * Calculate projected event times for each event and create a new EventTimetable class instance.
     * Add the EventTimetable instances to a list.
     *
     * @param eventList     a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @return              a list of EventTimetable instances with a subgroup, type, name, location, and projected event times
     */
    private static List<EventTimetable> getEventTimetables(List<Event> eventList) {
        List<EventTimetable> eventTimetables = new ArrayList<>();

        for (Event event : eventList) {
            List<LocalTime> projectedEventTimes = calculateProjectedTimes(event);
            eventTimetables.add(new EventTimetable(event.getSubgroup(), event.getType(), event.getName(), event.getLocation(), projectedEventTimes));
        }

        return eventTimetables;
    }

    /**
     * Determine if an event has a schedule or calculate projected times using the intitial start time and event frequency.
     * Add the schedule or projected event times to a list.
     *
     * @param event     the individual event data
     * @return          a list of LocalTime projected times
     */
    private static List<LocalTime> calculateProjectedTimes(Event event) {
        List<LocalTime> projectedTimesList = new ArrayList<>();

        String[] eventSchedule = event.getSchedule();

        if (eventSchedule != null) {
            for (String scheduleItem : eventSchedule) {
                LocalTime time = LocalTime.parse(scheduleItem);
                projectedTimesList.add(time);
            }
        }
        else {
            LocalTime initialStartTime = LocalTime.parse(event.getTime());
            int frequencyInHours = event.getFrequency();

            LocalTime projectedTime = initialStartTime.plusHours(frequencyInHours);
            projectedTimesList.add(projectedTime);

            while (!projectedTime.equals(initialStartTime)) {
                projectedTime = projectedTime.plusHours(frequencyInHours);
                projectedTimesList.add(projectedTime);
            }
        }

        return projectedTimesList;
    }

    /**
     * Get the zone ID of UTC and get the current time in UTC.
     *
     * @return      the current time in UTC
     */
    private static LocalTime getUtcTime() {
        ZoneId utcZoneId = ZoneId.of("UTC");
        return LocalTime.now(utcZoneId).truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Create a new TrackedEvent class instance based on the event notify state.
     *
     * @param event             the individual event data
     * @param notifyStates      the user preferences for notifications per event
     *
     * @return                  a new TrackedEvent instance with type and name strings
     */
    private static TrackedEvent getNewTrackedEvent(EventTimetable event, List<Pair<String, Boolean>> notifyStates) {
        TrackedEvent newEvent = null;
        String eventName = event.getName();

        for (Pair<String, Boolean> state : notifyStates) {
            String stateName = state.getKey();

            if (state.getValue()) {
                if (stateName.equals(eventName) && !stateName.equals(event.getSubgroup()) || stateName.contains(eventName) && stateName.contains(event.getLocation())) {
                    newEvent = new TrackedEvent(event.getType(), stateName);
                }
            }
        }

        return newEvent;
    }
}
