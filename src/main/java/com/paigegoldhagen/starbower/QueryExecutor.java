package com.paigegoldhagen.starbower;

import java.sql.*;
import java.time.LocalTime;

/**
 * Interface handling the preparation and execution of SQL queries to the database.
 */
public interface QueryExecutor {
    static void dropTable(Connection databaseConnection, Queries sqlQueries, String tableName) throws SQLException {
        String queryString = sqlQueries.getQueryString("DropTable");
        queryString = queryString.replace("?", tableName);
        Statement sqlStatement = databaseConnection.createStatement();
        sqlStatement.execute(queryString);
    }

    /**
     * Get the CreateTables query string and execute the SQL statement.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void createTables(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        String queryString = sqlQueries.getQueryString("CreateTables");
        Statement sqlStatement = databaseConnection.createStatement();
        sqlStatement.execute(queryString);
    }

    /**
     * Get the query string from the query name and execute the SQL statement.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param queryName             the query name string
     *
     * @return                      a ResultSet of table columns and rows
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static ResultSet getBasicResultSet(Connection databaseConnection, Queries sqlQueries, String queryName) throws SQLException {
        String queryString = sqlQueries.getQueryString(queryName);
        Statement sqlStatement = databaseConnection.createStatement();
        return sqlStatement.executeQuery(queryString);
    }

    /**
     * Get the query string from the query name, prepare a SQL statement using the column ID
     * and execute the prepared SQL statement.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param queryName             the query name string
     * @param columnID              the column ID to retrieve specific data
     *
     * @return                      a ResultSet of table columns and rows
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static ResultSet getSpecificResultSet(Connection databaseConnection, Queries sqlQueries, String queryName, Integer columnID) throws SQLException {
        String queryString = sqlQueries.getQueryString(queryName);
        PreparedStatement preparedSQLStatement = databaseConnection.prepareStatement(queryString);
        preparedSQLStatement.setInt(1, columnID);
        return preparedSQLStatement.executeQuery();
    }

    /**
     * Get the UpdateNotifyStateEnabled query string, prepare a SQL statement using
     * the NotifyState ID and NotifyState enabled boolean, and execute the prepared statement update.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param notifyStateID         the NotifyState ID to retrieve the correct row
     * @param notifyStateEnabled    the NotifyState enabled boolean to update
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void updateNotifyState(Connection databaseConnection, Queries sqlQueries, Integer notifyStateID, Boolean notifyStateEnabled) throws SQLException {
        String queryString = sqlQueries.getQueryString("UpdateNotifyStateEnabled");
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryString);
        preparedStatement.setBoolean(1, notifyStateEnabled);
        preparedStatement.setInt(2, notifyStateID);
        preparedStatement.executeUpdate();
    }

    /**
     * Get the UpdateScheduleTime query string, prepare a SQL statement using
     * the Schedule ID and Schedule time, and execute the prepared statement update.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param scheduleID            the Schedule ID to retrieve the correct row
     * @param scheduleTime          the Schedule time to update
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void updateScheduleTime(Connection databaseConnection, Queries sqlQueries, Integer scheduleID, LocalTime scheduleTime) throws SQLException {
        String queryString = sqlQueries.getQueryString("UpdateScheduleTime");
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(queryString);
        preparedStatement.setTime(1, Time.valueOf(scheduleTime));
        preparedStatement.setInt(2, scheduleID);
        preparedStatement.executeUpdate();
    }
}