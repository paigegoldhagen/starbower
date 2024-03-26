package com.paigegoldhagen.starbower;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Creating and updating Festival panel components using a repeating Runnable interface.
 */
public class FestivalComponents implements ComponentHandler, QueryHandler {
    /**
     * Schedule a Runnable to update the Festival components every minute.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalPanel         the panel to add Festival components
     * @param checkboxList          a list to manage all created checkboxes
     */
    public static void scheduleFestivalComponentUpdater(Connection databaseConnection, Queries sqlQueries, JPanel festivalPanel, List<JCheckBox> checkboxList) {
        Runnable updateFestivalComponents = updateFestivalComponents(databaseConnection, sqlQueries, festivalPanel, checkboxList);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(updateFestivalComponents, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Remove any out of date Festival components from the Festival panel,
     * populate the Festival panel with new Festival components, and repaint the Festival panel.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalPanel         the panel to add Festival components
     * @param checkboxList          a list to manage all created checkboxes
     *
     * @return                      the methods to run
     */
    private static Runnable updateFestivalComponents(Connection databaseConnection, Queries sqlQueries, JPanel festivalPanel, List<JCheckBox> checkboxList) {
        return () -> {
            removeOutOfDateComponents(festivalPanel, checkboxList);

            try {
                populateFestivalPanel(databaseConnection, sqlQueries, festivalPanel, checkboxList);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            refreshFestivalPanel(festivalPanel);
        };
    }

    /**
     * Remove all components from the Festival panel and remove any Festival panel checkboxes from the checkbox list.
     *
     * @param festivalPanel the panel to remove components
     * @param checkboxList  a list to manage all created checkboxes
     */
    private static void removeOutOfDateComponents(JPanel festivalPanel, List<JCheckBox> checkboxList) {
        List<Component> festivalComponentList = List.of(festivalPanel.getComponents());

        for (Component component : festivalComponentList) {
            festivalPanel.remove(component);

            if (component instanceof JCheckBox) {
                String componentName = component.getName();
                checkboxList.removeIf(checkbox -> checkbox.getName().equals(componentName));
            }
        }
        refreshFestivalPanel(festivalPanel);
    }

    /**
     * Validate and repaint the Festival panel after removing and updating the Festival panel components.
     *
     * @param festivalPanel the panel to repaint
     */
    private static void refreshFestivalPanel(JPanel festivalPanel) {
        festivalPanel.validate();
        festivalPanel.repaint();
    }

    /**
     * Get a list of Festivals and create countdown components based on the Festival ongoing boolean.
     * Create DynamicEvent components if a Festival has DynamicEvents.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalPanel         the panel to add the countdown components and DynamicEvent components
     * @param checkboxList          a list to add all created checkboxes
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void populateFestivalPanel(Connection databaseConnection, Queries sqlQueries, JPanel festivalPanel, List<JCheckBox> checkboxList) throws SQLException {
        LocalDateTime utcDate = getUtcDate();

        List<Festival> festivalList = QueryHandler.getFestivalList(databaseConnection, sqlQueries, utcDate);

        for (Festival festival : festivalList) {
            String festivalName = festival.getName();

            LocalDateTime startDate = festival.getStartDate();
            LocalDateTime endDate = festival.getEndDate();

            int rowCount = 0;

            if (festival.getIsOngoing()) {
                addCountdownComponents(festivalPanel, rowCount, festivalName, endDate, utcDate, festival.getIsOngoing());
                List<DynamicEvent> dynamicEventList = festival.getDynamicEventList();

                if (!dynamicEventList.isEmpty()) {
                    addDynamicEventComponents(databaseConnection, sqlQueries, festivalPanel, checkboxList, rowCount, festival, dynamicEventList);
                }
                break;
            }
            else if (startDate.equals(getUpcomingFestivalDate(databaseConnection, sqlQueries, utcDate))) {
                addCountdownComponents(festivalPanel, rowCount, festivalName, startDate, utcDate, festival.getIsOngoing());
                break;
            }
        }
    }

    /**
     * Update the Category panel layout and create DynamicEvent components based on the amount of Festival DynamicEvents.
     * Determine the Category name string based on the Festival DynamicEvent names and create a Category checkbox.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalPanel         the panel to add the Category checkbox and DynamicEvent components
     * @param checkboxList          a list to add all created checkboxes
     * @param rowCount              the current row within the layout
     * @param festival              a class for retrieving Festival information
     * @param dynamicEventList      a list of Festival DynamicEvents
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void addDynamicEventComponents(Connection databaseConnection, Queries sqlQueries, JPanel festivalPanel, List<JCheckBox> checkboxList, Integer rowCount, Festival festival, List<DynamicEvent> dynamicEventList) throws SQLException {
        GridBagConstraints layout = new GridBagConstraints();
        rowCount += 2;
        ComponentHandler.updateCategoryPanelLayout(layout);

        DynamicEvent firstDynamicEvent = dynamicEventList.getFirst();
        String firstDynamicEventName = firstDynamicEvent.getName();

        if (dynamicEventList.size() == 1) {
            addSingularDynamicEventComponents(festivalPanel, checkboxList, layout, rowCount, firstDynamicEventName, firstDynamicEvent.getNotifyStateID(), firstDynamicEvent.getNotifyStateEnabled(), firstDynamicEvent.getMapName());
        }
        else {
            String categoryName = getCategoryName(festival.getName(), dynamicEventList, dynamicEventList.getFirst().getName());
            addCategoryCheckbox(databaseConnection, sqlQueries, festivalPanel, checkboxList, layout, rowCount, festival.getCategoryID(), categoryName);

            rowCount += 1;
            addMultiDynamicEventComponents(festivalPanel, checkboxList, layout, rowCount, categoryName, dynamicEventList);
        }
    }

    /**
     * Get the Category information with the Festival Category ID.
     * Create a Category checkbox and a separator and add the components to the Festival panel.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param festivalPanel         the panel to add components
     * @param checkboxList          a list to add all created checkboxes
     * @param layout                a class for setting visual constraints for GUI components
     * @param rowCount              the current row within the layout
     * @param categoryID            the Festival Category ID
     * @param categoryName          the Festival name string
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void addCategoryCheckbox(Connection databaseConnection, Queries sqlQueries, JPanel festivalPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, Integer categoryID, String categoryName) throws SQLException {
        Category festivalCategory = QueryHandler.getFestivalCategory(databaseConnection, sqlQueries, categoryID);

        JCheckBox categoryCheckbox = ComponentHandler.createCategoryCheckbox(categoryName, festivalCategory.getNotifyStateID(), festivalCategory.getNotifyStateEnabled());
        rowCount += 1;
        ComponentHandler.addFestivalCategoryCheckbox(festivalPanel, checkboxList, layout, rowCount, categoryCheckbox);

        rowCount += 1;
        ComponentHandler.addSeparator(festivalPanel, layout, rowCount);
    }

    /**
     * Determine the DynamicEvent name and create a DynamicEvent checkbox with the name,
     * DynamicEvent NotifyState ID and NotifyState enabled boolean.
     * Create a location label with the DynamicEvent waypoint name string.
     *
     * @param festivalPanel     the panel to add the DynamicEvent components
     * @param checkboxList      a list to add all created checkboxes
     * @param layout            a class for setting visual constraints for GUI components
     * @param rowCount          the current row within the layout
     * @param categoryName      the Festival name string
     * @param dynamicEventList  a list of Festival DynamicEvents
     */
    private static void addMultiDynamicEventComponents(JPanel festivalPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, String categoryName, List<DynamicEvent> dynamicEventList) {
        rowCount += 2;
        int startRow = rowCount;

        for (DynamicEvent dynamicEvent : dynamicEventList) {
            String dynamicEventName = getDynamicEventName(categoryName, dynamicEvent);

            ComponentHandler.addDynamicEventCheckbox(festivalPanel, checkboxList, layout, rowCount, dynamicEventName, dynamicEvent.getNotifyStateID(), dynamicEvent.getNotifyStateEnabled());
            rowCount += 1;
            ComponentHandler.addLocationLabel(festivalPanel, layout, startRow, dynamicEvent.getWaypointName());
            startRow += 1;
        }
    }

    /**
     * Set the Category name string as the DynamicEvent name if the Festival DynamicEvents all have the same name.
     *
     * @param festivalName          the Festival name string
     * @param dynamicEventList      a list of Festival DynamicEvents
     * @param firstDynamicEventName the first DynamicEvent name string in the Festival DynamicEvent list
     *
     * @return                      the Category name string
     */
    private static String getCategoryName(String festivalName, List<DynamicEvent> dynamicEventList, String firstDynamicEventName) {
        String categoryName = festivalName;

        for (DynamicEvent dynamicEvent : dynamicEventList) {
            categoryName = dynamicEvent.getMapName();
            String dynamicEventName = dynamicEvent.getName();

            if (dynamicEventList.indexOf(dynamicEvent) > 0 && dynamicEventName.equals(firstDynamicEventName)) {
                categoryName = dynamicEventName;
                break;
            }
        }
        return categoryName;
    }

    /**
     * Get the zone ID of UTC and return the current date of the zone ID.
     *
     * @return the current date in UTC
     */
    private static LocalDateTime getUtcDate() {
        ZoneId utcZoneID = ZoneId.of("UTC");
        return LocalDateTime.now(utcZoneID).truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Set the Category panel layout and create a Category label using the Festival name string.
     * Add the Category label and a separator to the Festival panel.
     * Get the countdown string using the Festival date, UTC date and Festival ongoing boolean.
     * Create a countdown label with the countdown string and add the label to the Festival panel.
     *
     * @param festivalPanel the panel to add the components
     * @param rowCount      the current row within the layout
     * @param festivalName  the Festival name string
     * @param festivalDate  the date a Festival will start or end
     * @param utcDate       the current date in UTC
     * @param isOngoing     the Festival ongoing boolean
     */
    private static void addCountdownComponents(JPanel festivalPanel, Integer rowCount, String festivalName, LocalDateTime festivalDate, LocalDateTime utcDate, Boolean isOngoing) {
        GridBagConstraints layout = new GridBagConstraints();
        ComponentHandler.setCategoryPanelLayout(layout);

        ComponentHandler.addCategoryLabel(festivalPanel, layout, rowCount, festivalName);
        rowCount += 1;

        ComponentHandler.addSeparator(festivalPanel, layout, rowCount);
        rowCount += 1;

        String countdownString = CountdownFormatter.buildCountdownString(festivalDate, utcDate, isOngoing);
        ComponentHandler.addCountdownLabel(festivalPanel, layout, rowCount, countdownString);
    }

    /**
     * Create a DynamicEvent checkbox using the DynamicEvent name, NotifyState ID and NotifyState enabled boolean.
     * Create a location label using the DynamicEvent map name.
     *
     * @param festivalPanel                     the panel to add the DynamicEvent checkbox and location label
     * @param checkboxList                      a list to add all created checkboxes
     * @param layout                            a class for setting visual constraints for GUI components
     * @param rowCount                          the current row within the layout
     * @param dynamicEventName                  the DynamicEvent name string
     * @param dynamicEventNotifyStateID         the NotifyState ID
     * @param dynamicEventNotifyStateEnabled    the NotifyState enabled boolean
     * @param dynamicEventMapName               the map name string
     */
    private static void addSingularDynamicEventComponents(JPanel festivalPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, String dynamicEventName, Integer dynamicEventNotifyStateID, Boolean dynamicEventNotifyStateEnabled, String dynamicEventMapName) {
        rowCount += 1;
        int startRow = rowCount;

        ComponentHandler.addDynamicEventCheckbox(festivalPanel, checkboxList, layout, rowCount, dynamicEventName, dynamicEventNotifyStateID, dynamicEventNotifyStateEnabled);
        ComponentHandler.addLocationLabel(festivalPanel, layout, startRow, dynamicEventMapName);
    }

    /**
     * Determine the DynamicEvent name based on the DynamicEvent name and Category name.
     *
     * @param categoryName  the Category name string
     * @param dynamicEvent  a class for retrieving DynamicEvent information
     *
     * @return              a DynamicEvent name string
     */
    private static String getDynamicEventName(String categoryName, DynamicEvent dynamicEvent) {
        String dynamicEventName = dynamicEvent.getName();

        if (dynamicEventName.equals(categoryName)) {
            dynamicEventName = dynamicEvent.getMapName();
        }
        return dynamicEventName;
    }

    /**
     * Get a list of all Festival start dates and set the upcoming Festival date
     * as the next nearest date before the current date.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param utcDate               the current date in UTC
     *
     * @return                      the start date of the next Festival
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static LocalDateTime getUpcomingFestivalDate(Connection databaseConnection, Queries sqlQueries, LocalDateTime utcDate) throws SQLException {
        LocalDateTime upcomingFestivalDate = null;

        List<LocalDateTime> festivalStartDateList = QueryHandler.getFestivalStartDateList(databaseConnection, sqlQueries);

        for (LocalDateTime startDate : festivalStartDateList) {
            if (utcDate.isBefore(startDate)) {
                upcomingFestivalDate = startDate;
                break;
            }
        }
        return upcomingFestivalDate;
    }
}