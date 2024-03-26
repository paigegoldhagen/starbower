package com.paigegoldhagen.starbower;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Interface handling the execution of SQL queries
 * and setting NotifyState, Expansion, Category, Festival, DynamicEvent and Schedule class information.
 */
public interface QueryHandler extends QueryExecutor {
    /**
     * Create the database tables.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void createTables(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        QueryExecutor.createTables(databaseConnection, sqlQueries);
    }

    /**
     * Get all information from the NotifyState table and add new NotifyState classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of NotifyState classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<NotifyState> getNotifyStateList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<NotifyState> notifyStateList = new ArrayList<>();

        ResultSet notifyStates = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "NotifyStates");

        while (notifyStates.next()) {
            int notifyStateID = notifyStates.getInt("PK_NotifyStateID");
            boolean notifyStateEnabled = notifyStates.getBoolean("NotifyStateEnabled");

            notifyStateList.add(new NotifyState(notifyStateID, notifyStateEnabled));
        }
        return notifyStateList;
    }

    /**
     * Update a NotifyState using a NotifyState ID and NotifyState enabled boolean.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param notifyStateID         the NotifyState ID to retrieve the correct row
     * @param notifyStateEnabled    the NotifyState enabled boolean to update
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void updateNotifyState(Connection databaseConnection, Queries sqlQueries, Integer notifyStateID, Boolean notifyStateEnabled) throws SQLException {
        QueryExecutor.updateNotifyState(databaseConnection, sqlQueries, notifyStateID, notifyStateEnabled);
    }

    /**
     * Get all NotifyStates and save them to the Windows Registry.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void backupNotifyStates(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry) throws SQLException {
        ResultSet notifyStates = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "NotifyStates");

        while (notifyStates.next()) {
            windowsRegistry.putBoolean(notifyStates.getString("PK_NotifyStateID"), notifyStates.getBoolean("NotifyStateEnabled"));
        }
    }

    /**
     * Get the NotifyState ID of a Category.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param categoryID            the Category ID to retrieve the correct row
     *
     * @return                      a NotifyState ID from a Category
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getNotifyStateID(Connection databaseConnection, Queries sqlQueries, Integer categoryID) throws SQLException {
        ResultSet notifyStateID = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "NotifyStateID", categoryID);
        notifyStateID.next();
        return notifyStateID.getInt("FK_Category_NotifyState");
    }

    /**
     * Get the NotifyState enabled boolean of a NotifyState ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param notifyStateID         the NotifyState ID to retrieve the correct row
     *
     * @return                      a NotifyState enabled boolean
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Boolean getNotifyStateEnabled(Connection databaseConnection, Queries sqlQueries, Integer notifyStateID) throws SQLException {
        ResultSet notifyStateEnabled = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "NotifyStateEnabled", notifyStateID);
        notifyStateEnabled.next();
        return notifyStateEnabled.getBoolean("NotifyStateEnabled");
    }

    /**
     * Get all enabled NotifyState IDs and add them to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of enabled NotifyState IDs
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Integer> getEnabledNotifyStateIDList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<Integer> enabledNotifyStateIDList = new ArrayList<>();

        ResultSet enabledNotifyStates = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "EnabledNotifyStates");

        while (enabledNotifyStates.next()) {
            enabledNotifyStateIDList.add(enabledNotifyStates.getInt("PK_NotifyStateID"));
        }

        return enabledNotifyStateIDList;
    }

    /**
     * Get the NotifyState IDs from the Category table and add them to a list depending on the result.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of Category NotifyState IDs
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Integer> getCategoryNotifyStateIDList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<Integer> categoryNotifyStateIDList = new ArrayList<>();

        ResultSet categoryNotifyStateIDs = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "CategoryNotifyStateIDs");

        while (categoryNotifyStateIDs.next()) {
            int notifyStateID = categoryNotifyStateIDs.getInt("FK_Category_NotifyState");

            if (notifyStateID != 0) {
                categoryNotifyStateIDList.add(notifyStateID);
            }
        }
        return categoryNotifyStateIDList;
    }

    /**
     * Get a Category ID from a NotifyState ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param notifyStateID         the NotifyState ID to retrieve the correct row
     *
     * @return                      a Category ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getNotifyStateCategoryID(Connection databaseConnection, Queries sqlQueries, Integer notifyStateID) throws SQLException {
        ResultSet notifyStateCategoryID = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "NotifyStateCategoryID", notifyStateID);
        notifyStateCategoryID.next();
        return notifyStateCategoryID.getInt("PK_CategoryID");
    }

    /**
     * Get all enabled NotifyState IDs for Categories and DynamicEvents.
     * Get the DynamicEvent ID from each NotifyState ID and add it to a list
     * depending on if the NotifyState ID is associated with a Category or DynamicEvent.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of enabled DynamicEvent IDs
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Integer> getEnabledDynamicEventIDList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<Integer> enabledDynamicEventIDList = new ArrayList<>();

        List<Integer> enabledNotifyStateIDList = getEnabledNotifyStateIDList(databaseConnection, sqlQueries);
        List<Integer> categoryNotifyStateIDList = getCategoryNotifyStateIDList(databaseConnection, sqlQueries);

        for (Integer notifyStateID : enabledNotifyStateIDList) {
            if (!categoryNotifyStateIDList.contains(notifyStateID)) {
                ResultSet dynamicEventID = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "DynamicEventID", notifyStateID);
                dynamicEventID.next();
                enabledDynamicEventIDList.add(dynamicEventID.getInt("PK_DynamicEventID"));
            }
        }
        return enabledDynamicEventIDList;
    }

    /**
     * Get all information from the Expansion table and add new Expansion classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of Expansion classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Expansion> getExpansionList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<Expansion> expansionList = new ArrayList<>();

        ResultSet expansions = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "Expansions");

        while (expansions.next()) {
            expansionList.add(new Expansion(expansions.getInt("PK_ExpansionID"), expansions.getString("ExpansionName")));
        }
        return expansionList;
    }

    /**
     * Get the Expansion ID of a Category.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param categoryID            the Category ID to retrieve the correct row
     *
     * @return                      an Expansion ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getExpansionID(Connection databaseConnection, Queries sqlQueries, Integer categoryID) throws SQLException {
        ResultSet expansionID = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "ExpansionID", categoryID);
        expansionID.next();
        return expansionID.getInt("FK_Category_Expansion");
    }

    /**
     * Get the last (maximum) Expansion ID in the Expansion table.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      the last Expansion ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getLastExpansionID(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        ResultSet lastExpansion = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "LastExpansion");
        lastExpansion.next();
        return lastExpansion.getInt("LastExpansion");
    }

    /**
     * Get Categories from an Expansion ID and add new Category classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param expansionID           the Expansion ID to retrieve the correct row
     *
     * @return                      a list of Category classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Category> getCategoryList(Connection databaseConnection, Queries sqlQueries, Integer expansionID) throws SQLException {
        List<Category> categoryList = new ArrayList<>();

        ResultSet categories = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "Categories", expansionID);

        while (categories.next()) {
            Category category = new Category(categories.getInt("PK_CategoryID"), categories.getString("CategoryName"));
            categoryList.add(category);
        }
        return categoryList;
    }

    /**
     * Get the Category ID of a DynamicEvent.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param dynamicEventID        the DynamicEvent ID to retrieve the correct row
     *
     * @return                      a Category ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getCategoryID(Connection databaseConnection, Queries sqlQueries, Integer dynamicEventID) throws SQLException {
        ResultSet category = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "CategoryID", dynamicEventID);
        category.next();
        return category.getInt("FK_DynamicEvent_Category");
    }

    /**
     * Get the Category ID of the Category that has a rotational Schedule.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a Category ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Integer getRotationalCategoryID(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        ResultSet rotationalCategoryID = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "RotationalCategoryID");
        rotationalCategoryID.next();
        return rotationalCategoryID.getInt("PK_CategoryID");
    }

    /**
     * Get a Map ID from a Waypoint ID and get the Map name from the Map ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param waypointID            the Waypoint ID to retrieve the correct row
     *
     * @return                      a Map name string
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static String getMapName(Connection databaseConnection, Queries sqlQueries, Integer waypointID) throws SQLException {
        int mapID = getMapID(databaseConnection, sqlQueries, waypointID);
        ResultSet mapName = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "MapName", mapID);
        mapName.next();
        return mapName.getString("MapName");
    }

    /**
     * Get a Map ID from a Waypoint ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param waypointID            the Waypoint ID to retrieve the correct row
     *
     * @return                      a Map ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static Integer getMapID(Connection databaseConnection, Queries sqlQueries, Integer waypointID) throws SQLException {
        ResultSet mapID = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "MapID", waypointID);
        mapID.next();
        return mapID.getInt("FK_Waypoint_Map");
    }

    /**
     * Get all information from the Festival table, get the Festival name from the Category ID,
     * determine if the Festival is ongoing, and get a list of Festival DynamicEvents.
     * Add new Festival classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param utcDate               the current date in UTC
     *
     * @return                      a list of Festival classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Festival> getFestivalList(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate) throws SQLException {
        List<Festival> festivalList = new ArrayList<>();

        ResultSet festivals = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "Festivals");

        while (festivals.next()) {
            int categoryID = festivals.getInt("FK_Festival_Category");

            ResultSet festivalCategory = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "FestivalCategory", categoryID);
            festivalCategory.next();

            LocalDateTime startDate = festivals.getTimestamp("FestivalStart").toLocalDateTime();
            LocalDateTime endDate = festivals.getTimestamp("FestivalEnd").toLocalDateTime();

            boolean festivalOngoing = isFestivalOngoing(startDate, endDate, utcDate);

            List<DynamicEvent> dynamicEventList = getDynamicEventList(databaseConnection, sqlQueries, categoryID);

            Festival festival = new Festival(categoryID, festivalCategory.getString("CategoryName"), startDate, endDate, festivalOngoing, dynamicEventList);
            festivalList.add(festival);
        }
        return festivalList;
    }

    /**
     * Determine if a Festival is ongoing based on the start date, end date and current date.
     *
     * @param startDate the start date of a Festival
     * @param endDate   the end date of a Festival
     * @param utcDate   the current date in UTC
     *
     * @return          a boolean based on the date comparisons
     */
    private static Boolean isFestivalOngoing(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime utcDate) {
        return utcDate.isAfter(startDate) && utcDate.isBefore(endDate) || utcDate.equals(startDate) || utcDate.equals(endDate);
    }

    /**
     * Get a Category name and NotifyState ID from a Festival Category ID.
     * Get the NotifyState enabled boolean from the NotifyState ID.
     * Create a new Category class.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalCategoryID    the Festival Category ID to retrieve the correct row
     *
     * @return                      a Category class with Festival Category ID, Category Name, NotifyState ID and NotifyState enabled boolean
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Category getFestivalCategory(Connection databaseConnection, Queries sqlQueries, Integer festivalCategoryID) throws SQLException {
        ResultSet category = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "FestivalCategory", festivalCategoryID);
        category.next();

        String categoryName = category.getString("CategoryName");
        int notifyStateID = category.getInt("FK_Category_NotifyState");

        ResultSet notifyStateEnabled = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "NotifyStateEnabled", notifyStateID);
        notifyStateEnabled.next();

        return new Category(festivalCategoryID, categoryName, notifyStateID, notifyStateEnabled.getBoolean("NotifyStateEnabled"));
    }

    /**
     * Get all Festival start dates and add them to a list.
     * Sort the list in chronological order.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of Festival start dates in chronological order
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<LocalDateTime> getFestivalStartDateList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<LocalDateTime> festivalStartDateList = new ArrayList<>();

        ResultSet festivalStartDates = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "FestivalStartDates");

        while (festivalStartDates.next()) {
            LocalDateTime startDate = festivalStartDates.getTimestamp("FestivalStart").toLocalDateTime();
            festivalStartDateList.add(startDate);
        }
        Collections.sort(festivalStartDateList);
        return festivalStartDateList;
    }

    /**
     * Get all information from the DynamicEvent table of a Category ID.
     * Get the NotifyState enabled boolean from the NotifyState ID.
     * Get the Waypoint name and Map name from the Waypoint ID.
     * Add new DynamicEvent classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param categoryID            the Category ID to retrieve the correct row
     *
     * @return                      a list of DynamicEvent classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<DynamicEvent> getDynamicEventList(Connection databaseConnection, Queries sqlQueries, Integer categoryID) throws SQLException {
        List<DynamicEvent> dynamicEventList = new ArrayList<>();

        ResultSet dynamicEvents = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "DynamicEvents", categoryID);

        while (dynamicEvents.next()) {
            String dynamicEventName = dynamicEvents.getString("DynamicEventName");
            int notifyStateID = dynamicEvents.getInt("FK_DynamicEvent_NotifyState");
            int waypointID = dynamicEvents.getInt("FK_DynamicEvent_Waypoint");

            ResultSet notifyStateEnabled = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "NotifyStateEnabled", notifyStateID);
            notifyStateEnabled.next();

            ResultSet waypointName = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "WaypointName", waypointID);
            waypointName.next();

            String mapName = getMapName(databaseConnection, sqlQueries, waypointID);

            DynamicEvent dynamicEvent = new DynamicEvent(dynamicEventName, notifyStateID, notifyStateEnabled.getBoolean("NotifyStateEnabled"), mapName, waypointName.getString("WaypointName"));
            dynamicEventList.add(dynamicEvent);
        }
        return dynamicEventList;
    }

    /**
     * Get a list of Schedule IDs that operate on a rotational basis.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      a list of Schedule IDs
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Integer> getRotationalScheduleIDList(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        List<Integer> rotationalScheduleIDList = new ArrayList<>();

        ResultSet rotationalScheduleIDs = QueryExecutor.getBasicResultSet(databaseConnection, sqlQueries, "RotationalScheduleIDs");

        while (rotationalScheduleIDs.next()) {
            rotationalScheduleIDList.add(rotationalScheduleIDs.getInt("PK_ScheduleID"));
        }
        return rotationalScheduleIDList;
    }

    /**
     * Update the Schedule table using a Schedule ID and Schedule time.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param scheduleID            the Schedule ID to retrieve the correct row
     * @param scheduleTime          the Schedule time to update
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void updateScheduleTime(Connection databaseConnection, Queries sqlQueries, Integer scheduleID, LocalTime scheduleTime) throws SQLException {
        QueryExecutor.updateScheduleTime(databaseConnection, sqlQueries, scheduleID, scheduleTime);
    }

    /**
     * Get the Schedule time and frequency from a DynamicEvent ID and add new Schedule classes to a list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param dynamicEventID        the DynamicEvent ID to retrieve the correct row
     *
     * @return                      a list of Schedule classes
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static List<Schedule> getScheduleList(Connection databaseConnection, Queries sqlQueries, Integer dynamicEventID) throws SQLException {
        List<Schedule> scheduleList = new ArrayList<>();

        ResultSet schedules = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "Schedule", dynamicEventID);

        while (schedules.next()) {
            LocalTime time = schedules.getTime("ScheduleTime").toLocalTime();
            int frequency = schedules.getInt("ScheduleFrequency");
            scheduleList.add(new Schedule(time, frequency));
        }
        return scheduleList;
    }

    /**
     * Get the Category ID from an upcoming DynamicEvent ID.
     * Get the Expansion ID from the Category ID.
     * Create a new Category class.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param dynamicEventID        the DynamicEvent ID to retrieve the correct row
     *
     * @return                      a Category class with Category ID and Expansion ID
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static Category getUpcomingCategory(Connection databaseConnection, Queries sqlQueries, Integer dynamicEventID) throws SQLException {
        int categoryID = getCategoryID(databaseConnection, sqlQueries, dynamicEventID);
        int expansionID = getExpansionID(databaseConnection, sqlQueries, categoryID);
        return new Category(categoryID, expansionID);
    }

    /**
     * Get all information from the DynamicEvent table for an upcoming DynamicEvent ID.
     * Get the Map name from the Waypoint ID.
     * Create a new DynamicEvent class.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param dynamicEventID        the DynamicEvent ID to retrieve the correct row
     *
     * @return                      a DynamicEvent class with DynamicEvent name, DynamicEvent kind and Map name
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static DynamicEvent getUpcomingDynamicEvent(Connection databaseConnection, Queries sqlQueries, Integer dynamicEventID) throws SQLException {
        ResultSet dynamicEvent = QueryExecutor.getSpecificResultSet(databaseConnection, sqlQueries, "DynamicEvent", dynamicEventID);
        dynamicEvent.next();

        String dynamicEventName = dynamicEvent.getString("DynamicEventName");
        int dynamicEventKindID = dynamicEvent.getInt("FK_DynamicEvent_Kind");

        int waypointID = dynamicEvent.getInt("FK_DynamicEvent_Waypoint");
        String mapName = getMapName(databaseConnection, sqlQueries, waypointID);

        return new DynamicEvent(dynamicEventName, dynamicEventKindID, mapName);
    }
}