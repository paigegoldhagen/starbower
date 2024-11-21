package com.paigegoldhagen.starbower;

import java.util.List;

/**
 * Creating a Message class with a caption string and text string.
 */
public class MessageHandler {
    /**
     * Format the caption string and text string based on the amount of upcoming DynamicEvents.
     * Create a new Message class with the formatted caption and text strings.
     *
     * @param upcomingDynamicEventList  a list of upcoming DynamicEvents
     * @param notifyMinutes             the notification reminder time in minutes
     *
     * @return                          a populated Message class
     */
    public static Message getNotificationMessage(List<DynamicEvent> upcomingDynamicEventList, Integer notifyMinutes) {
        String notifyMinuteString = " in " + notifyMinutes + " minutes!";

        String messageCaption = null;
        String messageText = formatSingleLineString(upcomingDynamicEventList.getFirst()) + notifyMinuteString;

        if (upcomingDynamicEventList.size() > 1) {
            messageCaption = "Multiple events starting" + notifyMinuteString;
            messageText = formatMultiLineString(upcomingDynamicEventList);
        }
        return new Message(messageCaption, messageText);
    }

    /**
     * Get the upcoming DynamicEvent name and determine the flavour text
     * of a single-line notification using the upcoming DynamicEvent kind.
     *
     * @param upcomingDynamicEvent  a class for retrieving DynamicEvent information
     * @return                      the DynamicEvent name with flavour text
     */
    private static String formatSingleLineString(DynamicEvent upcomingDynamicEvent) {
        String eventName = upcomingDynamicEvent.getName();

        int eventKindID = upcomingDynamicEvent.getKindID();

        if (eventKindID >= 405) {
            eventName = eventName + " (" + upcomingDynamicEvent.getMapName() + ")";
        }

        String eventMessage = upcomingDynamicEvent.getKindMessage();
        return String.format(eventMessage, eventName);
    }

    /**
     * Build a multi-line string based on the number of upcoming DynamicEvents and the event display limit.
     * Append the last line of the string based on the number of remaining events.
     *
     * @param upcomingDynamicEventList  a list of upcoming DynamicEvents
     * @return                          a formatted multi-line string
     */
    private static String formatMultiLineString(List<DynamicEvent> upcomingDynamicEventList) {
        StringBuilder messageText = new StringBuilder();
        String lastEventToShow = null;

        int eventDisplayLimit = 2;
        int remainingEvents = 0;

        for (DynamicEvent dynamicEvent : upcomingDynamicEventList) {
            String eventName = dynamicEvent.getName();

            int eventKindID = dynamicEvent.getKindID();

            if (eventKindID >= 405) {
                eventName = eventName + " (" + dynamicEvent.getMapName() + ")";
            }

            int eventPosition = upcomingDynamicEventList.indexOf(dynamicEvent);

            if (eventPosition <= eventDisplayLimit) {
                messageText.append(eventName).append("\n");
            }
            else {
                lastEventToShow = eventName;
                remainingEvents = upcomingDynamicEventList.size() - eventPosition;
                break;
            }
        }
        appendLastLine(messageText, remainingEvents, lastEventToShow);
        return String.valueOf(messageText);
    }

    /**
     * Determine and append the last line of the multi-line string based on the number of remaining events.
     *
     * @param messageText       the multi-line string to append
     * @param remainingEvents   the number of remaining events left to display
     * @param lastEventToShow   the DynamicEvent name string of a single remaining event
     */
    private static void appendLastLine(StringBuilder messageText, Integer remainingEvents, String lastEventToShow) {
        if (remainingEvents == 1 && lastEventToShow != null) {
            messageText.append(lastEventToShow);
        }
        else if (remainingEvents != 0) {
            String footer = "...and " + remainingEvents + " more events";
            messageText.append(footer);
        }
    }
}