package com.paigegoldhagen.starbower;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Getting upcoming DynamicEvent classes.
 */
public class UpcomingEvents implements QueryHandler {
    /**
     * Get a list of enabled DynamicEvent IDs and get a list of Timetables using the enabled DynamicEvent IDs.
     * Calculate the notification time for each Timetable time using the notification minutes.
     * Add an upcoming DynamicEvent to a list if the notification time matches the current time in UTC.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param utcDate               the current date in UTC
     * @param notifyMinutes         the notification reminder time in minutes
     *
     * @return                      a list of upcoming DynamicEvent classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    public static List<DynamicEvent> getUpcomingDynamicEventList(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate, Integer notifyMinutes) throws SQLException {
        List<DynamicEvent> upcomingDynamicEventList = new ArrayList<>();

        List<Integer> enabledDynamicEventIDList = QueryHandler.getEnabledDynamicEventIDList(databaseConnection, sqlQueries);
        List<Timetable> timetableList = TimetableHandler.getTimetableList(databaseConnection, sqlQueries, enabledDynamicEventIDList);

        for (Timetable timetable : timetableList) {
            for (LocalTime time : timetable.getTimeList()) {
                LocalTime notifyTime = time.minusMinutes(notifyMinutes);

                if (notifyTime.equals(getUtcTime())) {
                    addUpcomingDynamicEvent(databaseConnection, sqlQueries, utcDate, upcomingDynamicEventList, timetable.getDynamicEventID());
                    break;
                }
            }
        }
        return upcomingDynamicEventList;
    }

    /**
     * Get an upcoming Category class using the DynamicEvent ID and get the last Expansion ID.
     * Get the current or next nearest Festival using the current date.
     * Get a new DynamicEvent class based on the Expansion ID, Category ID, and Festival ongoing boolean.
     * Add the DynamicEvent class to a list.
     *
     * @param databaseConnection        the connection to the Starbower relational database
     * @param sqlQueries                a class for retrieving SQL query strings
     * @param utcDate                   the current date in UTC
     * @param upcomingDynamicEventList  a list of upcoming DynamicEvents
     * @param dynamicEventID            the DynamicEvent ID from a Timetable
     *
     * @throws SQLException             the database could not be accessed or the table/column/row could not be found
     */
    private static void addUpcomingDynamicEvent(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate, List<DynamicEvent> upcomingDynamicEventList, Integer dynamicEventID) throws SQLException {
        Category upcomingCategory = QueryHandler.getUpcomingCategory(databaseConnection, sqlQueries, dynamicEventID);
        int lastExpansionID = QueryHandler.getLastExpansionID(databaseConnection, sqlQueries);

        Festival festival = getFestivalInformation(databaseConnection, sqlQueries, utcDate);

        if (upcomingCategory.getExpansionID() == lastExpansionID) {
            if (upcomingCategory.getID().equals(festival.getCategoryID()) && festival.getIsOngoing()) {
                DynamicEvent upcomingDynamicEvent = QueryHandler.getUpcomingDynamicEvent(databaseConnection, sqlQueries, dynamicEventID);
                upcomingDynamicEventList.add(upcomingDynamicEvent);
            }
        }
        else {
            DynamicEvent upcomingDynamicEvent = QueryHandler.getUpcomingDynamicEvent(databaseConnection, sqlQueries, dynamicEventID);
            upcomingDynamicEventList.add(upcomingDynamicEvent);
        }
    }

    /**
     * Get a list of Festival classes and create a new Festival class based on the ongoing boolean and current date.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param utcDate               the current date in UTC
     *
     * @return                      a Festival class with Festival Category ID and Festival ongoing boolean
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static Festival getFestivalInformation(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate) throws SQLException {
        int festivalCategoryID = 0;
        boolean festivalOngoing = false;

        List<Festival> festivalList = QueryHandler.getFestivalList(databaseConnection, sqlQueries, utcDate);

        for (Festival festival : festivalList) {
            int categoryID = festival.getCategoryID();
            boolean isOngoing = festival.getIsOngoing();

            if (isOngoing) {
                festivalCategoryID = categoryID;
                festivalOngoing = true;
                break;
            }
            else if (utcDate.isBefore(festival.getStartDate())) {
                festivalCategoryID = categoryID;
                break;
            }
        }
        return new Festival(festivalCategoryID, festivalOngoing);
    }

    /**
     * Get the zone ID of UTC and return the current time of the zone ID.
     *
     * @return  the current time in UTC
     */
    private static LocalTime getUtcTime() {
        ZoneId utcZoneID = ZoneId.of("UTC");
        return LocalTime.now(utcZoneID).truncatedTo(ChronoUnit.SECONDS);
    }
}