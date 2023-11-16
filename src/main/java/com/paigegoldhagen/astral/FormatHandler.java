package com.paigegoldhagen.astral;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * For mapping CSV data, casting variables to different types, and formatting data.
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
     * Calculate the default event notification preferences based on the number of individual events.
     *
     * @param eventsTimetable       the list of events with name, location, and projected event times
     * @return                      a string containing the default event preferences
     */
    public static String calculateDefaultEventPreferences(List<Object[]> eventsTimetable) {
        StringBuilder defaultEventChoices = new StringBuilder();
        for (Object[] event : eventsTimetable) {
            defaultEventChoices.append("1");
        }
        return String.valueOf(defaultEventChoices);
    }

    /**
     * Separate each character in the event choices string and add it to a list.
     *
     * @param eventChoices      the user preferences for receiving notifications per event
     * @return                  a list of event notification preferences
     */
    public static List<String> getEventPreferencesAsList(String eventChoices) {
        List<String> eventPrefsList = new ArrayList<>();

        for (int index = 0; index < eventChoices.length(); index++) {
            String eventPreference = String.valueOf(eventChoices.charAt(index));
            eventPrefsList.add(eventPreference);
        }
        return eventPrefsList;
    }

    /**
     * Put all event choice strings into one string.
     *
     * @param eventChoices      the user choices for receiving notifications per event
     * @return                  a string of event notification preferences
     */
    public static String getEventPreferencesAsString(List<String> eventChoices) {
        StringBuilder eventStringBuilder = new StringBuilder();

        for (String choice : eventChoices) {
            eventStringBuilder.append(choice);
        }
        return String.valueOf(eventStringBuilder);
    }

    /**
     * Get the name string and boolean state used for initialising checkbox contents
     * based on the event name and whether notifications are enabled.
     *
     * @param eventsTimetable       the list of events with name, location, and projected event times
     * @param eventChoices          the user choices for receiving notifications per event
     *
     * @return                      a list of checkbox parameters with the event name and notification state
     */
    public static List<Object[]> getCheckboxParameters(List<Object[]> eventsTimetable, List<String> eventChoices) {
        List<Object[]> checkboxParameters = new ArrayList<>();

        for (Object[] event : eventsTimetable) {
            String name = (String) event[0];
            String location = (String) event[1];

            int indexOfEvent = eventsTimetable.indexOf(event);
            String choice = eventChoices.get(indexOfEvent);
            boolean checkboxState = choice.equals("1");

            if (name.equals("Ley-Line Anomaly")) {
                name = name + " (" + location + ")";
            }

            Object[] checkboxData = new Object[]{name, checkboxState};
            checkboxParameters.add(checkboxData);
        }
        return checkboxParameters;
    }

    /**
     * Get the notification message string depending on the event name
     * and if more than one event is happening at the same time.
     *
     * @param upcomingEvents            the list of upcoming events with event name and location
     * @param minutesBeforeEvent        the notification reminder time in minutes
     *
     * @return                          the string to display in the notification popup
     */
    public static String getNotificationMessage(List<Object[]> upcomingEvents, String minutesBeforeEvent) {
        String message = null;
        String messageHeader = "Multiple events starting in " + minutesBeforeEvent + " minutes!\n";
        List<String> eventsList = new ArrayList<>();

        for (Object[] event : upcomingEvents) {
            String bossName = (String) event[0];
            String eventLocation = (String) event[1];
            eventsList.add(bossName);

            message = bossName + " will spawn in " + minutesBeforeEvent + " minutes!";

            if (bossName.equals("Ley-Line Anomaly")) {
                message = bossName + " will spawn in " + eventLocation + " in " + minutesBeforeEvent + " minutes!";
            }
        }
        if (eventsList.size() > 1) {
            message = messageHeader + eventsList.getFirst() + "\n" + eventsList.getLast();
        }
        return message;
    }
}
