package com.paigegoldhagen.starbower;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Adding listeners to checkboxes to trigger database updates and component repainting.
 */
public class CheckboxHandler implements QueryHandler {
    /**
     * Add a listener for each checkbox to update the NotifyState database table with the checkbox selected state.
     * Update the NotifyState table and repaint checkboxes if the checkbox changed was a Category checkbox.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param checkboxList          a list of all JCheckbox components displayed on the GUI
     */
    public static void addCheckboxListeners(Connection databaseConnection, Queries sqlQueries, List<JCheckBox> checkboxList) {
        for (JCheckBox checkbox : checkboxList) {
            checkbox.addItemListener(itemEventReceiver -> {
                int notifyStateID = Integer.parseInt(checkbox.getName());
                boolean notifyStateEnabled = checkbox.isSelected();

                try {
                    QueryHandler.updateNotifyState(databaseConnection, sqlQueries, notifyStateID, notifyStateEnabled);

                    List<Integer> categoryNotifyStateIDList = QueryHandler.getCategoryNotifyStateIDList(databaseConnection, sqlQueries);

                    for (Integer categoryNotifyStateID : categoryNotifyStateIDList) {
                        if (categoryNotifyStateID.equals(notifyStateID)) {
                            updateDynamicEventNotifyStates(databaseConnection, sqlQueries, checkboxList, notifyStateID, notifyStateEnabled);
                            break;
                        }
                    }
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    /**
     * Get the Category ID associated with the NotifyState ID, get a list of all DynamicEvents with that Category ID,
     * and update the NotifyState database table for each DynamicEvent NotifyState ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param checkboxList          a list of all JCheckbox components displayed on the GUI
     * @param notifyStateID         the NotifyState ID from the checkbox name
     * @param notifyStateEnabled    the selected state of the checkbox
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void updateDynamicEventNotifyStates(Connection databaseConnection, Queries sqlQueries, List<JCheckBox> checkboxList, Integer notifyStateID, Boolean notifyStateEnabled) throws SQLException {
        int categoryID = QueryHandler.getNotifyStateCategoryID(databaseConnection, sqlQueries, notifyStateID);
        List<DynamicEvent> dynamicEventList = QueryHandler.getDynamicEventList(databaseConnection, sqlQueries, categoryID);

        for (DynamicEvent dynamicEvent : dynamicEventList) {
            int dynamicEventNotifyStateID = dynamicEvent.getNotifyStateID();

            QueryHandler.updateNotifyState(databaseConnection, sqlQueries, dynamicEventNotifyStateID, notifyStateEnabled);
            updateCheckboxes(databaseConnection, sqlQueries, checkboxList, dynamicEventNotifyStateID);
        }
    }

    /**
     * Get the NotifyState Enabled boolean using the DynamicEvent NotifyState ID and set the checkbox selected to the result.
     * Validate and repaint the checkbox component.
     *
     * @param databaseConnection        the connection to the Starbower relational database
     * @param sqlQueries                a class for retrieving SQL query strings
     * @param checkboxList              a list of all JCheckbox components displayed on the GUI
     * @param dynamicEventNotifyStateID the NotifyState ID for a DynamicEvent
     *
     * @throws SQLException             the database could not be accessed or the table/column/row could not be found
     */
    private static void updateCheckboxes(Connection databaseConnection, Queries sqlQueries, List<JCheckBox> checkboxList, Integer dynamicEventNotifyStateID) throws SQLException {
        for (JCheckBox checkbox : checkboxList) {
            if (checkbox.getName().equals(String.valueOf(dynamicEventNotifyStateID))) {
                boolean notifyStateEnabled = QueryHandler.getNotifyStateEnabled(databaseConnection, sqlQueries, dynamicEventNotifyStateID);

                checkbox.setSelected(notifyStateEnabled);
                checkbox.validate();
                checkbox.repaint();
            }
        }
    }
}