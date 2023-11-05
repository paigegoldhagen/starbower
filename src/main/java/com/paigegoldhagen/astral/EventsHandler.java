package com.paigegoldhagen.astral;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * For getting the events from the CSV file, converting event times to local time,
 * and calculating projected event times.
 */
public class EventsHandler {
    public static List<Events> eventsList;
    public static List<Object[]> eventsTimetable;

    /**
     * Read the CSV file from the FileHandler class and map the data to the Events class.
     *
     * @return the Events class as a list of beans
     */
    private static List<Events> getEventsList() {
        InputStream filePath = FileHandler.readCsvFile();

        return new CsvToBeanBuilder<Events>(new InputStreamReader(filePath))
                .withType(Events.class)
                .build()
                .parse();
    }

    /**
     * Get the system timezone offset from the TimeHandler class
     * and calculate the local start times using the offset in minutes.
     *
     * @return the updated Events list with local start times
     */
    private static List<Events> getLocalStartTimes() {
        ZoneOffset timeZoneOffset = TimeHandler.getTimeZoneOffset();
        long offsetInMinutes = TimeUnit.SECONDS.toMinutes(timeZoneOffset.getTotalSeconds());

        eventsList = getEventsList();
        for (Events event : eventsList) {
            LocalTime startTime = LocalTime.parse(event.getTime());
            startTime = startTime.plusMinutes(offsetInMinutes);
            event.Time = String.valueOf(startTime);
        }
        return eventsList;
    }

    /**
     * Iterate through the Events list with local start times, calculate projected event times,
     * and add the updates events to the Events Timetable list.
     */
    public static void getProjectedEventTimes() {
        eventsTimetable = new ArrayList<>();

        eventsList = getLocalStartTimes();
        for (Events event : eventsList) {
            String bossName = event.getName();
            List<LocalTime> projectedEventTimes = calculateProjectedEventTimes(event);

            Object[] eventLine = new Object[]{bossName, projectedEventTimes};
            eventsTimetable.add(eventLine);
        }
    }

    /**
     * Calculate the projected event times over a 24-hour period
     * using the initial start time and the frequency of the event.
     *
     * @param event     the boss name, initial start time, and frequency of the event
     * @return          a list of LocalTime times associated with an event
     */
    private static List<LocalTime> calculateProjectedEventTimes(Events event) {
        LocalTime startTime = LocalTime.parse(event.getTime());
        Integer frequencyInHours = event.getFrequency();

        LocalTime projectedTime = startTime.plusHours(frequencyInHours);
        List<LocalTime> projectedEventTimes = new ArrayList<>();
        projectedEventTimes.add(projectedTime);

        while (!projectedTime.equals(startTime)) {
            projectedTime = projectedTime.plusHours(frequencyInHours);
            projectedEventTimes.add(projectedTime);
        }
        return projectedEventTimes;
    }
}
