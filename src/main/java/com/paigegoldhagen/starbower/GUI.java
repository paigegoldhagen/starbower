package com.paigegoldhagen.starbower;

import com.formdev.flatlaf.FlatLaf;
import com.paigegoldhagen.starbower.themes.StarbowerLAF;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Displaying the main GUI window.
 */
public class GUI implements ComponentHandler {
    /**
     * Initialise the custom look and feel, initialise the frame,
     * get the dropdown panel and tabbed pane, and add the components to the frame.
     * Set the frame to be visible and initialise the component focus.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the frame
     *
     * @throws SQLException         the database could not be accessed or the table/column/row could not be found
     */
    public static void displayGUI(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList) throws SQLException {
        initialiseLookAndFeel();

        JFrame frame = initialiseFrame(databaseConnection, sqlQueries, windowsRegistry, appIconList);

        JPanel dropdownPanel = getDropdownPanel(windowsRegistry);
        JTabbedPane tabbedPane = getTabbedPane(databaseConnection, sqlQueries);

        ComponentHandler.addComponentsToFrame(frame, dropdownPanel, tabbedPane);

        frame.setVisible(true);
        initialiseFocus(tabbedPane);
    }

    /**
     * Register the custom theme resource folder location and set up the custom look and feel.
     */
    private static void initialiseLookAndFeel() {
        FlatLaf.registerCustomDefaultsSource("themes");
        StarbowerLAF.setup();
    }

    /**
     * Create a frame using the AppIcons and add a listener to the frame.
     *
     * @param databaseConnection    the connection to the Starbower relational database
     * @param sqlQueries            a class for retrieving SQL query strings
     * @param windowsRegistry       the user preferences for Starbower in the Windows Registry
     * @param appIconList           a list of Images for the frame
     *
     * @return                      the customised frame
     */
    private static JFrame initialiseFrame(Connection databaseConnection, Queries sqlQueries, Preferences windowsRegistry, List<Image> appIconList) {
        JFrame frame = createFrame(appIconList);
        ComponentHandler.addFrameListener(databaseConnection, sqlQueries, windowsRegistry, frame);
        return frame;
    }

    /**
     * Create a new frame with a title.
     * Set the frame size, layout, resizable state, relative location and default close operation.
     * Set the icon images using the AppIcon Images.
     *
     * @param appIconList   a list of Images
     * @return              a customised frame
     */
    private static JFrame createFrame(List<Image> appIconList) {
        JFrame frame = new JFrame("Starbower");

        frame.setSize(820, 740);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImages(appIconList);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return frame;
    }

    /**
     * Create a dropdown panel, get the dropdown components and add the components to the dropdown panel.
     *
     * @param windowsRegistry   the user preferences for Starbower in the Windows Registry
     * @return                  the populated dropdown panel
     */
    public static JPanel getDropdownPanel(Preferences windowsRegistry) {
        JPanel dropdownPanel = new JPanel(new GridBagLayout());
        List<JComponent> dropdownComponentList = ComponentHandler.getDropdownComponentList(windowsRegistry);
        ComponentHandler.addDropdownComponents(dropdownPanel, dropdownComponentList);

        return dropdownPanel;
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
        tabbedPane.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc")));
    }

    /**
     * Once all GUI components have loaded, request the focus in the frame for the tabbed pane.
     *
     * @param tabbedPane    the tabbed pane to focus
     */
    private static void initialiseFocus(JTabbedPane tabbedPane) {
        SwingUtilities.invokeLater(tabbedPane::requestFocusInWindow);
    }
}