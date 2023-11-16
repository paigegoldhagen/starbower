package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * For creating the events timetable and retrieving upcoming events.
 */
public class EventsHandler {
    /**
     * Calculate projected event times and add the updated events to the events timetable.
     *
     * @param eventsList        the Events class as a list of beans
     * @return                  the events timetable with each event name, location, and projected event times
     */
    public static List<Object[]> getEventsTimetable(List<Events> eventsList) {
        List<Object[]> eventsTimetable = new ArrayList<>();

        for (Events event : eventsList) {
            String bossName = event.getName();
            String eventLocation = event.getLocation();
            List<LocalTime> projectedEventTimes = getProjectedEventTimes(event);

            Object[] eventLine = new Object[]{bossName, eventLocation, projectedEventTimes};
            eventsTimetable.add(eventLine);
        }
        return eventsTimetable;
    }

    /**
     * Get the scheduled event times or calculate the projected event times over a 24-hour period
     * using the initial start time and the frequency of the event.
     *
     * @param event         the event data to get the event schedule or initial start time and frequency
     * @return              a list of LocalTime times associated with an event
     */
    private static List<LocalTime> getProjectedEventTimes(Events event) {
        List<LocalTime> projectedEventTimes = new ArrayList<>();
        String[] eventSchedule = event.getSchedule();

        if (eventSchedule != null) {
            for (String time : eventSchedule) {
                LocalTime scheduledTime = LocalTime.parse(time);
                projectedEventTimes.add(scheduledTime);
            }
        } else {
            LocalTime startTime = LocalTime.parse(event.getTime());
            Integer frequencyInHours = event.getFrequency();

            LocalTime projectedTime = startTime.plusHours(frequencyInHours);
            projectedEventTimes.add(projectedTime);

            while (!projectedTime.equals(startTime)) {
                projectedTime = projectedTime.plusHours(frequencyInHours);
                projectedEventTimes.add(projectedTime);
            }
        }
        return projectedEventTimes;
    }

    /**
     * Store upcoming events in a list if the event notification time matches UTC time
     * and if notifications for the event are enabled.
     *
     * @param eventsTimetable       the list of events with name, location, and projected event times
     * @param timeChoice            the user choice for the notification reminder in minutes
     * @param eventChoices          the user choices for receiving notifications per event
     * @param utcTime               the current time in UTC
     *
     * @return                      a list of upcoming events
     */
    public static List<Object[]> getUpcomingEvents(List<Object[]> eventsTimetable, String timeChoice, List<String> eventChoices, LocalTime utcTime) {
        List<Object[]> upcomingEvents = new ArrayList<>();
        int minutesBeforeEvent = Integer.parseInt(timeChoice);

        for (Object[] event : eventsTimetable) {
            int indexOfEvent = eventsTimetable.indexOf(event);

            String bossName = (String) event[0];
            String eventLocation = (String) event[1];
            List<LocalTime> projectedEventTimes = (List<LocalTime>) event[2];

            for (LocalTime time : projectedEventTimes) {
                time = time.minusMinutes(minutesBeforeEvent);

                if (time.equals(utcTime)) {
                    String notificationChoice = eventChoices.get(indexOfEvent);

                    if (notificationChoice.equals("1")) {
                        Object[] eventDetails = new Object[]{bossName, eventLocation};
                        upcomingEvents.add(eventDetails);
                        break;
                    }
                }
            }
        }
        return upcomingEvents;
    }
}
