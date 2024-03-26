package com.paigegoldhagen.starbower;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Updating Schedule times.
 */
public class ScheduleHandler implements QueryHandler {
    /**
     * Get a list of rotational Schedule IDs.
     * Get the amount of rotations to perform based on the day of the week in UTC.
     * Rotate the Schedule IDs based on the amount of rotations to perform.
     * Update the Schedule times in the Schedule table.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param utcDate               the current date in UTC
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    public static void updateScheduleTable(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate) throws SQLException {
        List<Integer> rotationalScheduleIDList = QueryHandler.getRotationalScheduleIDList(databaseConnection, sqlQueries);

        int rotationsToPerform = utcDate.getDayOfWeek().getValue() - 1;

        for (int rotations = rotationsToPerform; rotations > 0; rotations--) {
            rotateScheduleIDs(rotationalScheduleIDList);
        }
        updateScheduleTime(databaseConnection, sqlQueries, rotationalScheduleIDList);
    }

    /**
     * Rotate the Schedule IDs 3 positions to the left.
     *
     * @param rotationalScheduleIDList  a list of Schedule IDs
     */
    private static void rotateScheduleIDs(List<Integer> rotationalScheduleIDList) {
        int positionsToRotate = 3;

        for (int loopCount = 0; loopCount < positionsToRotate; loopCount++) {
            int firstPosition = rotationalScheduleIDList.getFirst();
            int scheduleIDPosition;

            for (scheduleIDPosition = 0; scheduleIDPosition < rotationalScheduleIDList.size() - 1; scheduleIDPosition++) {
                rotationalScheduleIDList.set(scheduleIDPosition, rotationalScheduleIDList.get(scheduleIDPosition + 1));
            }
            rotationalScheduleIDList.set(scheduleIDPosition, firstPosition);
        }
    }

    /**
     * Determine the initial start time for each Schedule ID based on
     * the position of the Schedule ID in the list.
     * Update the Schedule table with the Schedule ID and Schedule time.
     *
     * @param databaseConnection        the connection to the Starbower relational database
     * @param sqlQueries                a class for retrieving SQL query strings
     * @param rotationalScheduleIDList  a list of rotated Schedule IDs
     *
     * @throws SQLException             the database could not be accessed or the table/column/row could not be found
     */
    private static void updateScheduleTime(Connection databaseConnection, Queries sqlQueries, List<Integer> rotationalScheduleIDList) throws SQLException {
        LocalTime initialStartTime = LocalTime.of(0, 30, 0);

        for (Integer scheduleID : rotationalScheduleIDList) {
            int hoursToAdd = rotationalScheduleIDList.indexOf(scheduleID);
            LocalTime scheduleTime = initialStartTime.plusHours(hoursToAdd);
            QueryHandler.updateScheduleTime(databaseConnection, sqlQueries, scheduleID, scheduleTime);
        }
    }
}