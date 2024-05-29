package com.paigegoldhagen.starbower;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Displaying the main GUI window.
 */
public class GUI implements ComponentHandler {
    /**
     * Initialise the frame, get the top panel containing dropdown panels, get the tabbed pane,
     * and add the components to the frame.
     * Set the frame to be visible and initialise the component focus.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the frame
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    public static void displayGUI(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList, List<Dropdown> dropdownList) throws SQLException {
        JFrame frame = initialiseFrame(databaseConnection, sqlQueries, windowsRegistry, appIconList);

        JPanel topPanel = getTopPanel(windowsRegistry, dropdownList, frame);
        JTabbedPane tabbedPane = getTabbedPane(databaseConnection, sqlQueries);

        ComponentHandler.addComponentsToFrame(frame, topPanel, tabbedPane);

        frame.setVisible(true);
        ComponentHandler.setFrameFocus(tabbedPane);
    }

    /**
     * Set up the custom look and feel using the ThemeHandler and create a frame using the app icons.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the frame
     *
     * @return                      the customised frame
     */
    private static JFrame initialiseFrame(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList) {
        ThemeHandler.initialiseLookAndFeel(windowsRegistry);
        return createFrame(databaseConnection, sqlQueries, windowsRegistry, appIconList);
    }

    /**
     * Create a frame, set the visuals and behaviour using the app icons, and add a listener.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the frame
     *
     * @return                      a customised frame
     */
    private static JFrame createFrame(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList) {
        JFrame frame = new JFrame("Starbower");
        setFrameVisualsAndBehaviour(appIconList, frame);
        ComponentHandler.addFrameListener(databaseConnection, sqlQueries, windowsRegistry, frame);

        return frame;
    }

    /**
     * Set the frame size, layout, resizable state, location in the window and default close operation.
     * Set the icon images using the app icons.
     *
     * @param appIconList   a list of Images for the frame
     * @param frame         the frame to customise
     */
    private static void setFrameVisualsAndBehaviour(List<Image> appIconList, JFrame frame) {
        frame.setSize(820, 740);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImages(appIconList);
    }

    /**
     * Create a dropdown panel for each Dropdown data set and add the panels to a top-level panel.
     *
     * @param windowsRegistry   the user preferences for Starbower in the Windows Registry
     * @param dropdownList      a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     * @param frame             the visual window for GUI components
     *
     * @return                  a panel containing the Dropdown panels
     */
    private static JPanel getTopPanel(Preferences windowsRegistry, List<Dropdown> dropdownList, JFrame frame) {
        JPanel topPanel = new JPanel(new GridBagLayout());

        List<JPanel> dropdownPanelList = new ArrayList<>();

        for (Dropdown dropdown : dropdownList) {
            JPanel dropdownPanel = createDropdownPanel(windowsRegistry, frame, dropdownList, dropdown);
            dropdownPanelList.add(dropdownPanel);
        }

        ComponentHandler.addDropdownPanelsToTopPanel(topPanel, dropdownPanelList);

        return topPanel;
    }

    /**
     * Get the data for the dropdown components from the Dropdown data set and create the dropdown selection box.
     * Create a dropdown panel and add the dropdown components to the panel using the ComponentHandler.
     *
     * @param windowsRegistry   the user preferences for Starbower in the Windows Registry
     * @param frame             the visual window for GUI components
     * @param dropdownList      a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     * @param dropdown          a single Dropdown data set
     *
     * @return                  a dropdown panel containing JLabels and a dropdown selection box
     */
    private static JPanel createDropdownPanel(Preferences windowsRegistry, JFrame frame, List<Dropdown> dropdownList, Dropdown dropdown) {
        String firstLabelText = dropdown.getFirstLabelText();
        String secondLabelText = dropdown.getSecondLabelText();

        JComboBox<String> dropdownSelectionBox = createDropdownSelectionBox(windowsRegistry, frame, dropdownList, dropdown);

        List<JComponent> dropdownComponentList = getDropdownComponentList(firstLabelText, secondLabelText, dropdownSelectionBox);

        return ComponentHandler.getDropdownPanel(dropdownComponentList);
    }

    /**
     * Create and add the JLabels based on the Dropdown data set and add the labels and dropdown selection box to a list.
     *
     * @param firstLabelText        a string for setting the first JLabel text
     * @param secondLabelText       a string for setting the second JLabel text
     * @param dropdownSelectionBox  a JComboBox of selectable items
     *
     * @return                      a list of dropdown components
     */
    private static List<JComponent> getDropdownComponentList(String firstLabelText, String secondLabelText, JComboBox<String> dropdownSelectionBox) {
        List<JComponent> dropdownComponentList = new ArrayList<>();

        dropdownComponentList.add(new JLabel(firstLabelText));
        dropdownComponentList.add(dropdownSelectionBox);

        if (secondLabelText != null) {
            dropdownComponentList.add(new JLabel(secondLabelText));
        }
        return dropdownComponentList;
    }

    /**
     * Add the selection data from the Dropdown data set to a new dropdown selection box.
     * Set the selected item based on the default or saved user preference
     * and set the preferred size of the dropdown selection box.
     * Add a listener to the dropdown selection box.
     *
     * @param windowsRegistry   the user preferences for Starbower in the Windows Registry
     * @param frame             the visual window for GUI components
     * @param dropdownList      a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     * @param dropdown          a single Dropdown data set
     *
     * @return                  a customised dropdown selection box
     */
    private static JComboBox<String> createDropdownSelectionBox(Preferences windowsRegistry, JFrame frame, List<Dropdown> dropdownList, Dropdown dropdown) {
        JComboBox<String> dropdownSelectionBox = new JComboBox<>(dropdown.getSelectionData());

        dropdownSelectionBox.setSelectedItem(windowsRegistry.get(dropdown.getPreferenceKey(), dropdown.getPreferenceValue()));
        dropdownSelectionBox.setPreferredSize(new Dimension(dropdown.getSizeWidth(), dropdown.getSizeHeight()));

        addDropdownSelectionBoxListener(windowsRegistry, frame, dropdownList, dropdown, dropdownSelectionBox);

        return dropdownSelectionBox;
    }

    /**
     * Add a listener to save the selected item in the Windows Registry.
     * Determine the dropdown data set and set the GUI theme using the ThemeHandler
     * and update the frame so the new theme can be displayed.
     *
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param frame                 the visual window for GUI components
     * @param dropdownList          a list of Dropdown classes containing data for JLabels and dropdown selection boxes
     * @param dropdown              a single Dropdown data set
     * @param dropdownSelectionBox  a JComboBox of selectable items
     */
    private static void addDropdownSelectionBoxListener(Preferences windowsRegistry, JFrame frame, List<Dropdown> dropdownList, Dropdown dropdown, JComboBox<String> dropdownSelectionBox) {
        dropdownSelectionBox.addItemListener(itemEventReceiver -> {
            String selectedItem = String.valueOf(dropdownSelectionBox.getSelectedItem());

            if (dropdown.equals(dropdownList.getFirst())) {
                windowsRegistry.put("Notify Minutes", selectedItem);
            }
            else {
                windowsRegistry.put("Theme", selectedItem);
                ThemeHandler.setTheme(selectedItem);
                SwingUtilities.updateComponentTreeUI(frame);
            }
        });
    }

    /**
     * Create a new tabbed pane, set the tabbed pane visuals, and populate the tabbed pane.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     *
     * @return                      the populated tabbed pane
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    private static JTabbedPane getTabbedPane(Connection databaseConnection, Queries sqlQueries) throws SQLException {
        JTabbedPane tabbedPane = new JTabbedPane();
        setTabbedPaneVisuals(tabbedPane);
        ComponentHandler.populateTabbedPane(databaseConnection, sqlQueries, tabbedPane);

        return tabbedPane;
    }

    /**
     * Set the tab placement, minimum tab width, FLatLaf style class and border colour of the tabbed pane.
     *
     * @param tabbedPane    the tabbed pane to customise
     */
    private static void setTabbedPaneVisuals(JTabbedPane tabbedPane) {
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.putClientProperty("JTabbedPane.minimumTabWidth", 210);
        tabbedPane.putClientProperty("FlatLaf.styleClass", "h2");
    }
}