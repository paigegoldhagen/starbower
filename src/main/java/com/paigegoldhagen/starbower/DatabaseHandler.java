package com.paigegoldhagen.starbower;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Establishing the connection to the Starbower embedded database,
 * creating the database tables and restoring saved preferences to the NotifyState table.
 */
public class DatabaseHandler implements QueryHandler {
    /**
     * Get the connection to the Starbower embedded database using the current directory URL and a basic administrator login.
     *
     * @return              the connection to the Starbower relational database
     * @throws SQLException the database could not be accessed or the table/column/row could not be found
     */
    public static Connection getDatabaseConnection() throws SQLException {
        String databaseURL = "jdbc:h2:./Starbower";
        return DriverManager.getConnection(databaseURL, "sa", "");
    }

    /**
     * Create the database tables and restore saved preferences from the Windows Registry.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    public static void populateDatabase(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry) throws SQLException {
        QueryHandler.createTables(databaseConnection, sqlQueries);
        restoreSavedPreferences(databaseConnection, sqlQueries, windowsRegistry);
    }

    /**
     * Compare the NotifyState table to the saved user preferences
     * and update the NotifyState table if the saved preferences are different.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void restoreSavedPreferences(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry) throws SQLException {
        List<NotifyState> notifyStateList = QueryHandler.getNotifyStateList(databaseConnection, sqlQueries);

        for (NotifyState notifyState : notifyStateList) {
            int notifyStateID = notifyState.getID();
            boolean notifyStateEnabled = notifyState.getIsEnabled();
            boolean savedPreference = windowsRegistry.getBoolean(String.valueOf(notifyStateID), notifyStateEnabled);

            if (notifyStateEnabled != savedPreference) {
                QueryHandler.updateNotifyState(databaseConnection, sqlQueries, notifyStateID, savedPreference);
            }
        }
    }
}