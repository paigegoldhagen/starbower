package com.paigegoldhagen.starbower;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Creating Timetable classes with projected times.
 */
public class TimetableHandler implements QueryHandler {
    /**
     * Create a new Timetable class for each enabled DynamicEvent ID and add them to a list.
     *
     * @param databaseConnection        the connection to the Starbower relational database
     * @param sqlQueries                a class for retrieving SQL query strings
     * @param enabledDynamicEventIDList a list of enabled DynamicEvent IDs
     *
     * @return                          a list of Timetable classes
     * @throws SQLException             the database could not be accessed or the table/column/row could not be found
     */
    public static List<Timetable> getTimetableList(Connection databaseConnection, Queries sqlQueries, List<Integer> enabledDynamicEventIDList) throws SQLException {
        List<Timetable> timetableList = new ArrayList<>();

        for (Integer dynamicEventID : enabledDynamicEventIDList) {
            Timetable timetable = createTimetable(databaseConnection, sqlQueries, dynamicEventID);
            timetableList.add(timetable);
        }
        return timetableList;
    }

    /**
     * Get the Schedule class list for a DynamicEvent ID, add the times to a list and set the frequency.
     * Calculate projected event times based on the time list size
     * and set the limit of event times in the time list based on the Category ID.
     * Create a new Timetable class.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param dynamicEventID        the DynamicEvent ID to retrieve the correct Schedule classes
     *
     * @return                      a Timetable class with a DynamicEvent ID and time list
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static Timetable createTimetable(Connection databaseConnection, Queries sqlQueries, Integer dynamicEventID) throws SQLException {
        List<LocalTime> timeList = new ArrayList<>();

        int frequencyInHours = 0;

        List<Schedule> scheduleList = QueryHandler.getScheduleList(databaseConnection, sqlQueries, dynamicEventID);

        for (Schedule schedule : scheduleList) {
            timeList.add(schedule.getTime());
            frequencyInHours = schedule.getFrequency();
        }

        if (timeList.size() == 1 || timeList.size() == 2) {
            timeList = calculateProjectedEventTimes(timeList, frequencyInHours);

            Integer categoryID = QueryHandler.getCategoryID(databaseConnection, sqlQueries, dynamicEventID);
            Integer rotationalCategoryID = QueryHandler.getRotationalCategoryID(databaseConnection, sqlQueries);

            if (categoryID.equals(rotationalCategoryID)) {
                timeList.subList(3, timeList.size()).clear();
            }
        }
        return new Timetable(dynamicEventID, timeList);
    }

    /**
     * Calculate the projected event times based on the intiial start time and frequency of the event.
     * Add the projected times to a list.
     *
     * @param timeList          a list of initial Timetable times
     * @param frequencyInHours  the frequency of a DynamicEvent
     *
     * @return                  a list of projected event times in chronological order
     */
    private static List<LocalTime> calculateProjectedEventTimes(List<LocalTime> timeList, Integer frequencyInHours) {
        List<LocalTime> projectedEventTimeList = new ArrayList<>();

        for (LocalTime time : timeList) {
            projectedEventTimeList.add(time);
            LocalTime projectedTime = time.plusHours(frequencyInHours);
            projectedEventTimeList.add(projectedTime);

            while (!projectedTime.equals(time)) {
                projectedTime = projectedTime.plusHours(frequencyInHours);

                if (!projectedTime.equals(time)) {
                    projectedEventTimeList.add(projectedTime);
                }
            }
        }
        return projectedEventTimeList;
    }
}