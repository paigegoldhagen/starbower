package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * For creating the events timetable.
 */
public class EventsHandler {
    /**
     * Iterate through the events list, calculate projected event times,
     * and add the updated events to the events timetable.
     *
     * @param eventsList    the Events class as a list of beans
     * @return              the events timetable with each event name, location, and projected times
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
     * @param event     the event data to get the event schedule or initial start time and frequency
     * @return          a list of LocalTime times associated with an event
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
     * Set the notification message string when an event time matches UTC time.
     *
     * @param eventsTimetable       the list of events with name, location, and projected event times
     * @param notificationBuffer    the user choice for the notification reminder in minutes
     * @param utcTime               the current time in UTC
     *
     * @return                      the string to display in the notification popup
     */
    public static String setEventMessage(List<Object[]> eventsTimetable, int notificationBuffer, LocalTime utcTime) {
        String message = null;
        String messageHeader = "Multiple events starting in " + notificationBuffer + " minutes!\n";
        List<String> upcomingEvents = new ArrayList<>();

        for (Object[] event : eventsTimetable) {
            String bossName = (String) event[0];
            String eventLocation = (String) event[1];
            List<LocalTime> projectedEventTimes = (List<LocalTime>) event[2];

            for (LocalTime time : projectedEventTimes) {
                time = time.minusMinutes(notificationBuffer);

                if (time.equals(utcTime)) {
                    String eventDetails = bossName;
                    message = bossName + " will spawn in " + notificationBuffer + " minutes!";

                    if (bossName.equals("Ley-Line Anomaly")) {
                        eventDetails = bossName + " in " + eventLocation;
                        message = bossName + " will spawn in " + eventLocation + " in " + notificationBuffer + " minutes!";
                    }

                    upcomingEvents.add(eventDetails);
                    break;
                }
            }
        }
        if (upcomingEvents.size() > 1) {
            message = messageHeader + upcomingEvents.getFirst() + "\n" + upcomingEvents.getLast();
        }
        return message;
    }
}
