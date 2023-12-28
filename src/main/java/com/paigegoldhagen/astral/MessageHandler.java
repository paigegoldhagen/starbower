package com.paigegoldhagen.astral;

import java.util.List;

/**
 * For dynamically formatting a notification message.
 */
public class MessageHandler {
    /**
     * Create a new Message class instance based on the number of tracked events.
     *
     * @param trackedEvents             a list of tracked events with a type and a name
     * @param notifyMinutes             the notification reminder time in minutes
     * @param trackedEventListSize      the numerical size of the tracked events list
     *
     * @return                          a new Message instance with type and name strings
     */
    public static Message getNotificationMessage(List<TrackedEvent> trackedEvents, String notifyMinutes, Integer trackedEventListSize) {
        String minuteString = " in " + notifyMinutes + " minutes!";

        String topLine = formatSingleLineString(trackedEvents.getFirst()) + minuteString;
        String lines = null;

        if (trackedEventListSize > 1) {
            topLine = "Multiple events starting" + minuteString;
            lines = formatMultiLineString(trackedEvents, trackedEventListSize);
        }

        return new Message(topLine, lines);
    }

    /**
     * Determine the flavour text based on the event type.
     *
     * @param event     the individual tracked event
     * @return          a message string with an event name and flavour text
     */
    private static String formatSingleLineString(TrackedEvent event) {
        String eventName = event.getName();

        return switch (event.getType()) {
            case "World Boss" -> eventName + " will spawn";
            case "Adventure" -> "The " + eventName + " adventure will begin";
            default -> eventName + " is starting";
        };
    }

    /**
     * Build a multi-line string based on the event display limit and the tracked event list size.
     *
     * @param trackedEvents             a list of tracked events with a type and a name
     * @param trackedEventListSize      the numerical size of the tracked events list
     *
     * @return                          a multi-line string of event names and (optional) footer string
     */
    private static String formatMultiLineString(List<TrackedEvent> trackedEvents, Integer trackedEventListSize) {
        StringBuilder lines = new StringBuilder();
        String lastEventToShow = null;

        int eventDisplayLimit = 2;
        int remainingEvents = 0;

        for (TrackedEvent event : trackedEvents) {
            String eventName = event.getName();

            int eventPosition = trackedEvents.indexOf(event);

            if (eventPosition <= eventDisplayLimit) {
                lines.append(eventName).append("\n");
            }
            else {
                lastEventToShow = eventName;
                remainingEvents = trackedEventListSize - eventPosition;
                break;
            }
        }

        return finaliseLines(lines, lastEventToShow, remainingEvents);
    }

    /**
     * Determine and append the last line onto the multi-line StringBuilder based on the number of remaining events.
     *
     * @param lines                 the multi-line StringBuilder
     * @param lastEventToShow       an event name string
     * @param remainingEvents       the number of remaining events to add
     *
     * @return                      the updated multi-line string
     */
    private static String finaliseLines(StringBuilder lines, String lastEventToShow, Integer remainingEvents) {
        if (remainingEvents == 1 && lastEventToShow != null) {
            lines.append(lastEventToShow);
        }
        else if (remainingEvents != 0) {
            String footer = "...and " + remainingEvents + " more events";
            lines.append(footer);
        }

        return String.valueOf(lines);
    }
}
