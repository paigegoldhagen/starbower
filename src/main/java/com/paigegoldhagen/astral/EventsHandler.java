package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * For calculating projected event times and getting the upcoming events list.
 */
public class EventsHandler {
    /**
    * Get the scheduled event times or calculate the projected event times over a 24-hour period
    * using the initial start time and the frequency of the event.
    *
    * @param event    the event data to get the event schedule or initial start time and frequency
    * @return         a list of LocalTime times associated with an event
    */
    public static List<LocalTime> getProjectedEventTimes(Events event) {
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
     * Store upcoming events in the UpcomingEvents class if the event notification time matches UTC time
     * and if notifications for the event are enabled.
     *
     * @param timetable      the list of events with name, location, and projected event times
     * @param notifyTime     the user choice for the notification reminder in minutes
     * @param notifyState    the user choices for receiving notifications per event
     * @param utcTime        the current time in UTC
     *
     * @return               a list of upcoming events
     */
    public static List<UpcomingEvents> getUpcomingEvents(List<Timetable> timetable, String notifyTime, String notifyState, LocalTime utcTime) {
        List<String> stateList = new ArrayList<>();
        for (int i = 0; i < notifyState.length(); i++) {
            String state = String.valueOf(notifyState.charAt(i));
            stateList.add(state);
        }

        List<UpcomingEvents> upcomingEvents = new ArrayList<>();
        for (Timetable t : timetable) {
            int indexOfEvent = timetable.indexOf(t);
            List<LocalTime> eventTimes = t.getTimes();

            for (LocalTime time : eventTimes) {
                time = time.minusMinutes(Integer.parseInt(notifyTime));

                if (time.equals(utcTime)) {
                    String indexOfState = stateList.get(indexOfEvent);

                    if (indexOfState.equals("1")) {
                        UpcomingEvents event = new UpcomingEvents(t.getName(), t.getLocation());
                        upcomingEvents.add(event);
                        break;
                    }
                }
            }
        }
        return upcomingEvents;
    }
}
