package com.paigegoldhagen.astral;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * For mapping CSV data and formatting the notification message string.
 */
public class FormatHandler {
    /**
     * Map the data from the CSV file to the Events class.
     *
     * @param eventsData    the input stream of the CSV file
     * @return              the Events class as a list of beans
     */
    public static List<Events> mapCsvToBeans(InputStream eventsData) {
        return new CsvToBeanBuilder<Events>(new InputStreamReader(eventsData))
                .withType(Events.class)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .build()
                .parse();
    }

    /**
     * Get the notification message string depending on the event name
     * and if more than one event is happening at the same time.
     *
     * @param upcomingEvents    the list of upcoming events with event name and location
     * @param notifyTime        the notification reminder time in minutes
     *
     * @return                  the string to display in the notification popup
     */
    public static String getNotificationMessage(List<UpcomingEvents> upcomingEvents, String notifyTime) {
        String message = null;
        String header = "Multiple events starting in " + notifyTime + " minutes!\n";
        List<String> eventsList = new ArrayList<>();

        for (UpcomingEvents e : upcomingEvents) {
            String eventName = e.getName();
            eventsList.add(eventName);

            message = eventName + " will spawn in " + notifyTime + " minutes!";

            if (eventName.equals("Ley-Line Anomaly")) {
                message = eventName + " will spawn in " + e.getLocation() + " in " + notifyTime + " minutes!";
            }
        }

        if (eventsList.size() > 1) {
            message = header + eventsList.getFirst() + "\n" + eventsList.getLast();
        }
        return message;
    }
}
