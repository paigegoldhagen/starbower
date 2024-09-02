package com.paigegoldhagen.starbower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Interface handling the creation and layout of GUI components.
 */
public interface ComponentHandler extends LayoutHandler, QueryHandler {
    /**
     * Add a listener to the frame to backup the NotifyState table preferences
     * to the Windows Registry when the frame window is in the process of closing.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param frame                 the visual window for GUI components
     */
    static void addFrameListener(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, JFrame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEventReceiver) {
                try {
                    QueryHandler.backupNotifyStates(databaseConnection, sqlQueries, windowsRegistry);
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Set the Dropdown panel layout and add the dropdown components to the panel.
     *
     * @param dropdownComponentList a list of JLabels and a dropdown selection box
     * @return                      a panel populated with the dropdown components
     */
    static JPanel getDropdownPanel(List<JComponent> dropdownComponentList) {
        JPanel dropdownPanel = newPanel();
        GridBagConstraints layout = new GridBagConstraints();
        LayoutHandler.setDropdownPanelLayout(layout);

        for (JComponent component : dropdownComponentList) {
            dropdownPanel.add(component, layout);
        }
        return dropdownPanel;
    }

    /**
     * Set the Dropdown panel layout and add the panels to the top panel.
     * Update the Dropdown panel layout for each panel added.
     *
     * @param topPanel          the panel to add the dropdown panels
     * @param dropdownPanelList a list of JPanels containing JLabels and a dropdown selection box
     */
    static void addDropdownPanelsToTopPanel(JPanel topPanel, List<JPanel> dropdownPanelList) {
        GridBagConstraints layout = new GridBagConstraints();
        LayoutHandler.setDropdownPanelLayout(layout);

        for (JPanel panel : dropdownPanelList) {
            LayoutHandler.updateDropdownPanelLayout(layout, dropdownPanelList, panel);
            topPanel.add(panel, layout);
        }
    }

    /**
     * Get a list of all Expansions and create an expansion panel using the Expansion ID.
     * Create a scroll pane with the expansion panel and add the scroll pane to the tabbed pane.
     * Add a listener to the tabbed pane.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param tabbedPane            a collection of scroll pane components separated by tabs
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    static void populateTabbedPane(Connection databaseConnection, Queries sqlQueries, JFrame frame, JTabbedPane tabbedPane) throws SQLException {
        List<Expansion> expansionList = QueryHandler.getExpansionList(databaseConnection, sqlQueries);

        for (Expansion expansion : expansionList) {
            JPanel expansionPanel = createExpansionPanel(databaseConnection, sqlQueries, frame, expansion.getID());
            JScrollPane scrollPane = createScrollPane(expansionPanel);

            tabbedPane.add(expansion.getName(), scrollPane);
            addTabbedPaneListener(tabbedPane, scrollPane);
        }
    }

    /**
     * Get a list of category panels using the Expansion ID and create an Expansion panel using the category panels.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param expansionID           an Expansion ID to get related categories
     *
     * @return                      an Expansion panel containing Category panels
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static JPanel createExpansionPanel(Connection databaseConnection, Queries sqlQueries, JFrame frame, Integer expansionID) throws SQLException {
        List<JPanel> categoryPanelList = getCategoryPanelList(databaseConnection, sqlQueries, frame, expansionID);
        return getExpansionPanel(categoryPanelList);
    }

    /**
     * Set the Expansion panel layout and add the Category panels to the Expansion panel.
     *
     * @param categoryPanelList a list of Category panels
     * @return                  an Expansion panel containing Category panels
     */
    private static JPanel getExpansionPanel(List<JPanel> categoryPanelList) {
        JPanel expansionPanel = newPanel();
        GridBagConstraints layout = new GridBagConstraints();
        LayoutHandler.setExpansionPanelLayout(layout);

        addCategoryPanelsToExpansionPanel(expansionPanel, categoryPanelList, layout);
        return expansionPanel;
    }

    /**
     * Create a scroll pane and viewport panel, set the viewport layout,
     * add the Expansion panel to the viewport panel, and set the scroll pane behaviour.
     *
     * @param expansionPanel    an Expansion panel containing Category Panels
     * @return                  a scroll pane with a viewport
     */
    private static JScrollPane createScrollPane(JPanel expansionPanel) {
        JScrollPane scrollPane = new JScrollPane();
        JPanel viewportPanel = newPanel();

        GridBagConstraints layout = new GridBagConstraints();
        LayoutHandler.setViewportLayout(layout);

        viewportPanel.add(expansionPanel, layout);

        setScrollPaneBehaviour(scrollPane, viewportPanel);
        return scrollPane;
    }

    /**
     * Set the viewport view with the viewport panel, set the scroll bar appearance,
     * and set the scrolling speed.
     *
     * @param scrollPane    a scroll pane
     * @param viewportPanel the panel to set as the viewport
     */
    private static void setScrollPaneBehaviour(JScrollPane scrollPane, JPanel viewportPanel) {
        scrollPane.setViewportView(viewportPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
    }

    /**
     * Add a listener to the tabbed pane to reset the scroll position back to the top
     * and request focus in the frame when switching tabs.
     *
     * @param tabbedPane    the collection of scroll pane components separated by tabs
     * @param scrollPane    a scroll pane with a viewport
     */
    private static void addTabbedPaneListener(JTabbedPane tabbedPane, JScrollPane scrollPane) {
        tabbedPane.addChangeListener(changeEventReceiver -> {
            scrollPane.getVerticalScrollBar().setValue(0);
            setFrameFocus(tabbedPane);
        });
    }

    /**
     * Once all GUI components have loaded and/or finished processing tasks,
     * request focus in the frame for the tabbed pane.
     *
     * @param tabbedPane    the tabbed pane to focus
     */
    static void setFrameFocus(JTabbedPane tabbedPane) {
        SwingUtilities.invokeLater(tabbedPane::requestFocusInWindow);
    }

    /**
     * Create Category panels with the Expansion ID and add listeners to all created checkboxes.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param expansionID           an Expansion ID to get related categories
     *
     * @return                      a list of populated Category panels
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static List<JPanel> getCategoryPanelList(Connection databaseConnection, Queries sqlQueries, JFrame frame, Integer expansionID) throws SQLException {
        List<JPanel> categoryPanelList = new ArrayList<>();
        List<JCheckBox> checkboxList = new ArrayList<>();

        createCategoryPanels(databaseConnection, sqlQueries, frame, expansionID, categoryPanelList, checkboxList);
        CheckboxHandler.addCheckboxListeners(databaseConnection, sqlQueries, checkboxList);

        return categoryPanelList;
    }

    /**
     * Get Categories associated with the Expansion ID and create Category panels for each Category.
     * Get the Expansion ID of the last Expansion and create a festival panel if the Expansion ID is the last expansion ID.
     * Add the created panels to the category panel list.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param expansionID           an Expansion ID to get related categories
     * @param categoryPanelList     the list to add the Category panels
     * @param checkboxList          the list to add all created checkboxes
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void createCategoryPanels(Connection databaseConnection, Queries sqlQueries, JFrame frame, Integer expansionID, List<JPanel> categoryPanelList, List<JCheckBox> checkboxList) throws SQLException {
        Integer lastExpansionID = QueryHandler.getLastExpansionID(databaseConnection, sqlQueries);
        List<Category> categoryList = QueryHandler.getCategoryList(databaseConnection, sqlQueries, expansionID);

        for (Category category : categoryList) {
            if (expansionID.equals(lastExpansionID)) {
                JPanel festivalPanel = newPanel();
                FestivalComponents.scheduleFestivalComponentUpdater(databaseConnection, sqlQueries, frame, festivalPanel, checkboxList);
                categoryPanelList.add(festivalPanel);
                break;
            }
            else {
                JPanel categoryPanel = newPanel();
                populateCategoryPanel(databaseConnection, sqlQueries, frame, categoryPanel, checkboxList, category);
                categoryPanelList.add(categoryPanel);
            }
        }
    }

    /**
     * Set the Category panel layout, add Category components to the panel, add a separator,
     * and add DynamicEvent components to the panel.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param categoryPanel         the panel to add Category components, separator and DynamicEvent components
     * @param checkboxList          a list to add all created checkboxes
     * @param category              a class for retrieving Category information
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void populateCategoryPanel(Connection databaseConnection, Queries sqlQueries, JFrame frame, JPanel categoryPanel, List<JCheckBox> checkboxList, Category category) throws SQLException {
        GridBagConstraints layout = new GridBagConstraints();
        LayoutHandler.setCategoryPanelLayout(layout);

        int rowCount = 0;

        int categoryID = category.getID();
        String categoryName = category.getName();

        addCategoryComponents(databaseConnection, sqlQueries, categoryPanel, checkboxList, layout, rowCount, categoryID, categoryName);

        rowCount += 1;
        addSeparator(categoryPanel, layout, rowCount);
        rowCount += 1;

        addDynamicEventComponents(databaseConnection, sqlQueries, frame, categoryPanel, checkboxList, layout, rowCount, categoryID, categoryName);
    }

    /**
     * Get the NotifyState ID of a Category.
     * Create a Category checkbox or label depending on the NotifyState ID.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param categoryPanel         the panel to add Category components
     * @param checkboxList          a list to add all created checkboxes
     * @param layout                a class for setting visual constraints for GUI components
     * @param rowCount              the current row within the layout
     * @param categoryID            the Category ID to get the NotifyState ID
     * @param categoryName          the Category name string to create a Category checkbox or label
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void addCategoryComponents(Connection databaseConnection, Queries sqlQueries, JPanel categoryPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, Integer categoryID, String categoryName) throws SQLException {
        int categoryNotifyStateID = QueryHandler.getNotifyStateID(databaseConnection, sqlQueries, categoryID);

        if (categoryNotifyStateID != 0) {
            boolean categoryNotifyStateEnabled = QueryHandler.getNotifyStateEnabled(databaseConnection, sqlQueries, categoryNotifyStateID);
            addCategoryCheckbox(categoryPanel, checkboxList, layout, rowCount, categoryName, categoryNotifyStateID, categoryNotifyStateEnabled);
        }
        else {
            addCategoryLabel(categoryPanel, layout, rowCount, categoryName);
        }
    }

    /**
     * Create a Category checkbox using the Category name, NotifyState ID and NotifyState enabled boolean,
     * set the Category checkbox layout, and add the checkbox to the checkbox list and Category panel.
     *
     * @param categoryPanel                 the panel to add the Category checkbox
     * @param checkboxList                  the list to add all created checkboxes
     * @param layout                        a class for setting visual constraints for GUI components
     * @param rowCount                      the current row within the layout
     * @param categoryName                  the Category name string
     * @param categoryNotifyStateID         the NotifyState ID
     * @param categoryNotifyStateEnabled    the NotifyState enabled boolean
     */
    private static void addCategoryCheckbox(JPanel categoryPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, String categoryName, Integer categoryNotifyStateID, Boolean categoryNotifyStateEnabled) {
        JCheckBox categoryCheckbox = createCategoryCheckbox(categoryName, categoryNotifyStateID, categoryNotifyStateEnabled);
        LayoutHandler.setCategoryCheckboxLayout(layout, rowCount);

        checkboxList.add(categoryCheckbox);
        categoryPanel.add(categoryCheckbox, layout);
    }

    /**
     * Create a Category checkbox with the Category name as the text and the NotifyState enabled boolean as the selected state.
     * Set the checkbox name as the NotifyState ID and define the FlatLaf style class of the checkbox.
     *
     * @param categoryName                  the Category name string
     * @param categoryNotifyStateID         the NotifyState ID
     * @param categoryNotifyStateEnabled    the NotifyState enabled boolean
     *
     * @return                              a populated Category checkbox
     */
    static JCheckBox createCategoryCheckbox(String categoryName, Integer categoryNotifyStateID, Boolean categoryNotifyStateEnabled) {
        JCheckBox categoryCheckbox = new JCheckBox(categoryName, categoryNotifyStateEnabled);
        categoryCheckbox.setName(String.valueOf(categoryNotifyStateID));
        categoryCheckbox.putClientProperty("FlatLaf.styleClass", "h3");

        return categoryCheckbox;
    }

    /**
     * Set the Category label layout, create a Category label using the Category name,
     * and add the label to the Category panel.
     *
     * @param categoryPanel the panel to add the Category label
     * @param layout        a class for setting visual constraints for GUI components
     * @param rowCount      the current row within the layout
     * @param categoryName  the Category name string
     */
    static void addCategoryLabel(JPanel categoryPanel, GridBagConstraints layout, Integer rowCount, String categoryName) {
        LayoutHandler.setCategoryLabelLayout(layout, rowCount);
        JLabel categoryLabel = createCategoryLabel(categoryName);
        categoryPanel.add(categoryLabel, layout);
    }

    /**
     * Create a Category label with the Category name as the text and define the FlatLaf style class of the label.
     *
     * @param categoryName  the Category name string
     * @return              a populated Category label
     */
    private static JLabel createCategoryLabel(String categoryName) {
        JLabel categoryLabel = new JLabel(categoryName);
        categoryLabel.putClientProperty("FlatLaf.styleClass", "h3");
        return categoryLabel;
    }

    /**
     * Set the separator layout and add a new separator to the Category panel.
     *
     * @param categoryPanel the panel to add the separator component
     * @param layout        a class for setting visual constraints for GUI components
     * @param rowCount      the current row within the layout
     */
    static void addSeparator(JPanel categoryPanel, GridBagConstraints layout, Integer rowCount) {
        LayoutHandler.setSeparatorLayout(layout, rowCount);
        categoryPanel.add(new JSeparator(), layout);
    }

    /**
     * Update the Category panel layout and get the DynamicEvents associated with a Category ID.
     * For each DynamicEvent, determine the DynamicEvent name and location and create a DynamicEvent checkbox and location button.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param frame                 the visual window for GUI components
     * @param categoryPanel         the panel to add DynamicEvent components
     * @param checkboxList          a list to add all created checkboxes
     * @param layout                a class for setting visual constraints for GUI components
     * @param rowCount              the current row within the layout
     * @param categoryID            the Category ID to get a list of DynamicEvents
     * @param categoryName          the Category name string
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static void addDynamicEventComponents(Connection databaseConnection, Queries sqlQueries, JFrame frame, JPanel categoryPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, Integer categoryID, String categoryName) throws SQLException {
        LayoutHandler.updateCategoryPanelLayout(layout);
        int startRow = rowCount;

        List<DynamicEvent> dynamicEventList = QueryHandler.getDynamicEventList(databaseConnection, sqlQueries, categoryID);

        for (DynamicEvent dynamicEvent : dynamicEventList) {
            String dynamicEventName = dynamicEvent.getName();
            String mapName = dynamicEvent.getMapName();
            String waypointName = dynamicEvent.getWaypointName();
            String waypointLink = dynamicEvent.getWaypointLink();
            String tooltipText = "Copy Waypoint link";

            String dynamicEventLocation = mapName;

            if (dynamicEventName.equals(categoryName)) {
                dynamicEventName = mapName;
                dynamicEventLocation = waypointName;
            }
            else if (mapName.equals(categoryName)) {
                dynamicEventLocation = waypointName;
            }

            if (dynamicEventLocation.equals(mapName)) {
                tooltipText = "Copy nearest Waypoint link";
            }

            addDynamicEventCheckbox(categoryPanel, checkboxList, layout, rowCount, dynamicEventName, dynamicEvent.getNotifyStateID(), dynamicEvent.getNotifyStateEnabled());
            rowCount += 1;
            addLocationButton(frame, categoryPanel, layout, startRow, dynamicEventLocation, waypointLink, tooltipText);
            startRow += 1;
        }
    }

    /**
     * Create a DynamicEvent checkbox using the DynamicEvent name, NotifyState ID, and NotifyState enabled boolean.
     * Set the DynamicEvent checkbox layout and add the checkbox to the checkbox list and Category panel.
     *
     * @param categoryPanel                     the panel to add the DynamicEvent checkbox
     * @param checkboxList                      a list to add all created checkboxes
     * @param layout                            a class for setting visual constraints for GUI components
     * @param rowCount                          the current row within the layout
     * @param dynamicEventName                  the DynamicEvent name string
     * @param dynamicEventNotifyStateID         the NotifyState ID
     * @param dynamicEventNotifyStateEnabled    the NotifyState enabled boolean
     */
    static void addDynamicEventCheckbox(JPanel categoryPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, String dynamicEventName, Integer dynamicEventNotifyStateID, Boolean dynamicEventNotifyStateEnabled) {
        JCheckBox dynamicEventCheckbox = createDynamicEventCheckbox(dynamicEventName, dynamicEventNotifyStateID, dynamicEventNotifyStateEnabled);
        LayoutHandler.setDynamicEventCheckboxLayout(layout, rowCount);

        checkboxList.add(dynamicEventCheckbox);
        categoryPanel.add(dynamicEventCheckbox, layout);
    }

    /**
     * Create a DynamicEvent checkbox with the DynamicEvent name as the text and NotifyState enabled boolean as the selected state.
     * Set the checkbox name as the NotifyState ID.
     *
     * @param dynamicEventName                  the DynamicEvent name string
     * @param dynamicEventNotifyStateID         the NotifyState ID
     * @param dynamicEventNotifyStateEnabled    the NotifyState enabled boolean
     *
     * @return                                  a populated DynamicEvent checkbox
     */
    private static JCheckBox createDynamicEventCheckbox(String dynamicEventName, Integer dynamicEventNotifyStateID, Boolean dynamicEventNotifyStateEnabled) {
        JCheckBox eventCheckbox = new JCheckBox(dynamicEventName, dynamicEventNotifyStateEnabled);
        eventCheckbox.setName(String.valueOf(dynamicEventNotifyStateID));
        return eventCheckbox;
    }

    /**
     * Create a location button using the DynamicEvent location, waypoint link, and tooltip text.
     * Add listeners to the button, set the button layout, and add the button to the Category panel.
     *
     * @param frame                 the visual window for GUI components
     * @param categoryPanel         the panel to add the button
     * @param layout                a class for setting visual constraints for GUI components
     * @param startRow              the starting row within the layout
     * @param dynamicEventLocation  the Map name or Waypoint name string
     * @param waypointLink          the Waypoint chat link string
     * @param tooltipText           the tooltip text string for the button
     */
    static void addLocationButton(JFrame frame, JPanel categoryPanel, GridBagConstraints layout, Integer startRow, String dynamicEventLocation, String waypointLink, String tooltipText) {
        JButton locationButton = createLocationButton(dynamicEventLocation, waypointLink, tooltipText);
        ButtonHandler.addLocationButtonListeners(frame, locationButton);

        LayoutHandler.setLocationButtonLayout(layout, startRow);
        categoryPanel.add(locationButton, layout);
    }

    /**
     * Create a location button using the DynamicEvent location.
     * Set the button name as the Waypoint link string and set the tooltip text.
     * Set the horizontal alignment of the button contents.
     *
     * @param dynamicEventLocation  the Map name or Waypoint name string
     * @param waypointLink          the Waypoint chat link string
     * @param tooltipText           the tooltip text string for the button
     *
     * @return                      a populated location button
     */
    private static JButton createLocationButton(String dynamicEventLocation, String waypointLink, String tooltipText) {
        JButton locationButton = new JButton(dynamicEventLocation);
        locationButton.setName(waypointLink);
        locationButton.setToolTipText(tooltipText);
        locationButton.setHorizontalAlignment(SwingConstants.RIGHT);
        return locationButton;
    }

    /**
     * Set the Category panel layout using the LayoutHandler interface.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void setCategoryPanelLayout(GridBagConstraints layout) {
        LayoutHandler.setCategoryPanelLayout(layout);
    }

    /**
     * Create a countdown label with the countdown string as the label text,
     * set the countdown label layout, and add the label to the countdown panel.
     *
     * @param countdownPanel    the panel to add the countdown label
     * @param layout            a class for setting visual constraints for GUI components
     * @param rowCount          the current row within the layout
     * @param countdownString   a string with the amount of time until a certain date
     */
    static void addCountdownLabel(JPanel countdownPanel, GridBagConstraints layout, Integer rowCount, String countdownString) {
        JLabel countdownLabel = new JLabel(countdownString);
        LayoutHandler.setCountdownLabelLayout(layout, rowCount);
        countdownPanel.add(countdownLabel, layout);
    }

    /**
     * Update the Category panel layout using the LayoutHandler interface.
     *
     * @param layout    a class for setting visual constraints for GUI components
     */
    static void updateCategoryPanelLayout(GridBagConstraints layout) {
        LayoutHandler.updateCategoryPanelLayout(layout);
    }

    /**
     * Set the Category checkbox layout and update the Y spacing for the layout.
     * Add the Category checkbox to the checkbox list and the Festival panel.
     *
     * @param festivalPanel     the panel to add the Category checkbox
     * @param checkboxList      a list to add all created checkboxes
     * @param layout            a class for setting visual constraints for GUI components
     * @param rowCount          the current row within the layout
     * @param categoryCheckbox  a populated Festival Category checkbox
     */
    static void addFestivalCategoryCheckbox(JPanel festivalPanel, List<JCheckBox> checkboxList, GridBagConstraints layout, Integer rowCount, JCheckBox categoryCheckbox) {
        LayoutHandler.setCategoryCheckboxLayout(layout, rowCount);
        LayoutHandler.updateYSpacing(layout);

        checkboxList.add(categoryCheckbox);
        festivalPanel.add(categoryCheckbox, layout);
    }

    /**
     * Add checkbox listeners for the Checkbox list using the CheckboxHandler.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param checkboxList          a list of all JCheckbox components displayed on the GUI
     */
    static void addCheckboxListeners(Connection databaseConnection, Queries sqlQueries, List<JCheckBox> checkboxList) {
        CheckboxHandler.addCheckboxListeners(databaseConnection, sqlQueries, checkboxList);
    }

    /**
     * Create a new panel with a GridBagLayout manager.
     *
     * @return  a panel with a specific layout manager
     */
    static JPanel newPanel() {
        return new JPanel(new GridBagLayout());
    }

    /**
     * Add the Category panels to the Expansion panel depending on the Category panel order.
     *
     * @param expansionPanel    the panel to add the Category panels
     * @param categoryPanelList the list of populated Category panels
     * @param layout            a class for setting visual constraints for GUI components
     */
    private static void addCategoryPanelsToExpansionPanel(JPanel expansionPanel, List<JPanel> categoryPanelList, GridBagConstraints layout) {
        for (JPanel categoryPanel : categoryPanelList) {
            LayoutHandler.updateExpansionPanelLayout(layout, categoryPanelList, categoryPanel);
            expansionPanel.add(categoryPanel, layout);
        }
    }

    /**
     * Initialise the frame layout and add the dropdown panel to the frame.
     * Update the frame layout and add the tabbed pane to the frame.
     *
     * @param frame         the visual window for GUI components
     * @param dropdownPanel the populated dropdown panel
     * @param tabbedPane    the tabbed pane with populated scroll panes and tabs
     */
    static void addComponentsToFrame(JFrame frame, JPanel dropdownPanel, JTabbedPane tabbedPane) {
        GridBagConstraints layout = new GridBagConstraints();

        LayoutHandler.initialiseFrameLayout(layout);
        frame.add(dropdownPanel, layout);

        LayoutHandler.updateFrameLayout(layout);
        frame.add(tabbedPane, layout);
    }
}