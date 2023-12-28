package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * The graphical user interface for the app.
 */
public class GUI {
    /**
     * Get access to the Windows Registry using the Preferences class.
     * Create the frame, create the dropdown panel and tabbed pane, and add the components to the frame.
     * Make the frame visible and set the initial focused component.
     *
     * @param festivalComponents        a title label and a countdown label for a festival
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param uniqueEventGroups         a list of unique event group names
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param appIcons                  a list of Image files
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     */
    public static void displayGUI(FestivalComponents festivalComponents, List<EventComponents> eventComponentsList, List<String> uniqueEventGroups, List<String> specialtySubgroupList, List<Image> appIcons, UserPreferences userPrefs) {
        Preferences windowsRegistry = Preferences.userNodeForPackage(Astral.class);
        JFrame frame = createFrame(appIcons);

        JPanel dropdownPanel = getDropdownPanel(userPrefs, windowsRegistry);
        JTabbedPane tabbedPane = getTabbedPane(festivalComponents, eventComponentsList, uniqueEventGroups, specialtySubgroupList, userPrefs, windowsRegistry);
        addComponentsToFrame(frame, dropdownPanel, tabbedPane);

        frame.setVisible(true);

        initialiseFocus(tabbedPane);
    }

    /**
     * Create a new JFrame and set the frame visuals and behaviour.
     *
     * @param appIcons      a list of Image files
     * @return              the customised JFrame
     */
    private static JFrame createFrame(List<Image> appIcons) {
        JFrame frame = new JFrame("Astral");

        frame.setSize(650, 600);
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImages(appIcons);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return frame;
    }

    /**
     * Set the layout parameters for the frame and add the dropdown panel and tabbed pane components to the frame.
     *
     * @param frame             the visual window for the GUI components
     * @param dropdownPanel     the panel with the dropdown component
     * @param tabbedPane        the collection of event checkboxes and labels separated by tab groups
     */
    private static void addComponentsToFrame(JFrame frame, JPanel dropdownPanel, JTabbedPane tabbedPane) {
        GridBagConstraints layout = new GridBagConstraints();

        initialiseFrameLayout(layout);
        frame.add(dropdownPanel, layout);

        updateFrameLayout(layout);
        frame.add(tabbedPane, layout);
    }

    /**
     * Set the frame anchor, fill, insets, and internal Y padding.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void initialiseFrameLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.NORTH;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.insets = new Insets(5, 5, 12, 10);
        layout.ipady = 15;
    }

    /**
     * Update the frame fill, insets, grid Y position, and X/Y weights.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void updateFrameLayout(GridBagConstraints layout) {
        layout.fill = GridBagConstraints.BOTH;
        layout.insets = new Insets(0, 0, 0, 0);
        layout.gridy = 1;
        layout.weightx = 1;
        layout.weighty = 1;
    }

    /**
     * Request focus for the tabbed pane when the app finishes loading.
     *
     * @param tabbedPane    the collection of event checkboxes and labels separated by tab groups
     */
    private static void initialiseFocus(JTabbedPane tabbedPane) {
        SwingUtilities.invokeLater(tabbedPane::requestFocusInWindow);
    }

    /**
     * Set the layout parameters for the dropdown panel.
     * Create new reminder labels, create the dropdown component, and add the components to a list.
     * Add each component in the list to the dropdown panel.
     *
     * @param userPrefs         the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry   the path to the Windows Registry data for the app
     *
     * @return                  the populated dropdown JPanel
     */
    private static JPanel getDropdownPanel(UserPreferences userPrefs, Preferences windowsRegistry) {
        JPanel dropdownPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        setDropdownPanelLayout(layout);

        JLabel leftReminder = new JLabel("Remind me");
        JLabel rightReminder = new JLabel("minutes before an event starts");
        JComboBox<String> dropdownComponent = createDropdownComponent(userPrefs, windowsRegistry);

        List<JComponent> componentList = new ArrayList<>(List.of(leftReminder, dropdownComponent, rightReminder));

        for (JComponent component : componentList) {
            if (component.equals(componentList.getLast())) {
                layout.weightx = 1;
            }
            layout.gridx += 1;
            dropdownPanel.add(component, layout);
        }

        return dropdownPanel;
    }

    /**
     * Set the dropdown panel anchor, insets, and X weight.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void setDropdownPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.WEST;
        layout.insets = new Insets(0, 8, 0, 0);
        layout.weightx = 0;
    }

    /**
     * Create a new JComboBox component with string choices representing notification minutes.
     * Set the selected item as the default or saved notification minutes from the user preferences.
     * Set the preferred size of the component and add an item listener.
     *
     * @param userPrefs         the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry   the path to the Windows Registry data for the app
     *
     * @return                  the populated dropdown JComboBox
     */
    private static JComboBox<String> createDropdownComponent(UserPreferences userPrefs, Preferences windowsRegistry) {
        JComboBox<String> dropdownComponent = new JComboBox<>(new String[]{"5", "10", "15", "20", "30", "60"});
        dropdownComponent.setSelectedItem(userPrefs.getNotifyMinutes());
        dropdownComponent.setPreferredSize(new Dimension(47, 21));

        addDropdownListener(dropdownComponent, userPrefs, windowsRegistry);

        return dropdownComponent;
    }

    /**
     * Add an item listener to get the selected item and save the choice in the user preferences and Windows Registry.
     *
     * @param dropdownComponent     the dropdown JComboBox
     * @param userPrefs             the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry       the path to the Windows Registry data for the app
     */
    private static void addDropdownListener(JComboBox<String> dropdownComponent, UserPreferences userPrefs, Preferences windowsRegistry) {
        dropdownComponent.addItemListener(itemEventReceiver -> {
            String notifyMinutes = String.valueOf(dropdownComponent.getSelectedItem());
            userPrefs.setNotifyMinutes(notifyMinutes);
            saveMinutesPreference(userPrefs, windowsRegistry);
        });
    }

    /**
     * Save the notify minutes user preference to the Windows Registry.
     *
     * @param userPrefs         the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry   the path to the Windows Registry data for the app
     */
    private static void saveMinutesPreference(UserPreferences userPrefs, Preferences windowsRegistry) {
        windowsRegistry.put("Notification Minutes", userPrefs.getNotifyMinutes());
    }

    /**
     * Create a JTabbedPane and set the visuals.
     * Create a new JScrollPane for each unique event group and add it to the tabbed pane as a tab.
     * Add change listeners to the tabbed pane.
     *
     * @param festivalComponents        a title label and a countdown label for a festival
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param uniqueEventGroups         a list of unique event group names
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     *
     * @return                          the populated JTabbedPane
     */
    private static JTabbedPane getTabbedPane(FestivalComponents festivalComponents, List<EventComponents> eventComponentsList, List<String> uniqueEventGroups, List<String> specialtySubgroupList, UserPreferences userPrefs, Preferences windowsRegistry) {
        JTabbedPane tabbedPane = new JTabbedPane();
        setTabbedPaneVisuals(tabbedPane);

        for (String eventGroup : uniqueEventGroups) {
            JScrollPane scrollPane = createScrollPane(festivalComponents, eventComponentsList, eventGroup, uniqueEventGroups.getLast(), specialtySubgroupList, userPrefs, windowsRegistry);
            tabbedPane.addTab(eventGroup, scrollPane);
            addTabbedPaneListener(tabbedPane, scrollPane);
        }
        return tabbedPane;
    }

    /**
     * Set the tabbed pane visuals.
     *
     * @param tabbedPane    the collection of event checkboxes and labels separated by tab groups
     */
    private static void setTabbedPaneVisuals(JTabbedPane tabbedPane) {
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.putClientProperty("JTabbedPane.minimumTabWidth", 210);
        tabbedPane.putClientProperty("FlatLaf.styleClass", "large");
        tabbedPane.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc")));
    }

    /**
     * Add a change listener to reset the scroll pane position upon switching tabs.
     *
     * @param tabbedPane    the collection of event checkboxes and labels separated by tab groups
     * @param scrollPane    a scrollable view of checkboxes and labels for a specific event group
     */
    private static void addTabbedPaneListener(JTabbedPane tabbedPane, JScrollPane scrollPane) {
        tabbedPane.addChangeListener(changeEventReceiver -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    /**
     * Create a new viewport JPanel and set the panel layout parameters.
     * Get the event group panel for the unique event group and add it to the viewport panel.
     * Based on the unique event group, update the panel layout parameters, get the festival panel and add it to the viewport panel.
     * Create a new JScrollPane and set the behaviour.
     *
     * @param festivalComponents        a title label and a countdown label for a festival
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param uniqueEventGroup          the unique event group
     * @param lastEventGroup            the last event group name
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     *
     * @return                          the populated JScrollPane
     */
    private static JScrollPane createScrollPane(FestivalComponents festivalComponents, List<EventComponents> eventComponentsList, String uniqueEventGroup, String lastEventGroup, List<String> specialtySubgroupList, UserPreferences userPrefs, Preferences windowsRegistry) {
        JPanel viewportPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        setPanelLayout(layout);

        JPanel eventGroupPanel = getEventGroupPanel(eventComponentsList, uniqueEventGroup, specialtySubgroupList, userPrefs, windowsRegistry);
        viewportPanel.add(eventGroupPanel, layout);

        if (festivalComponents != null && uniqueEventGroup.equals(lastEventGroup)) {
            updatePanelLayout(layout);
            JPanel festivalPanel = populateFestivalPanel(festivalComponents);
            viewportPanel.add(festivalPanel, layout);
        }

        JScrollPane scrollPane = new JScrollPane();
        setScrollPaneBehaviour(scrollPane, viewportPanel);

        return scrollPane;
    }

    /**
     * Set the panel anchor, fill, and X/Y weights.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void setPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1;
        layout.weighty = 1;
    }

    /**
     * Update the panel grid X and Y positions.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void updatePanelLayout(GridBagConstraints layout) {
        layout.gridx = 0;
        layout.gridy = 0;
    }

    /**
     * Set the viewport of the scroll pane, show the vertical scroll bar, and set the scrolling speed.
     *
     * @param scrollPane        a scrollable view of checkboxes and labels for a specific event group
     * @param viewportPanel     the panel to be viewed through the scroll pane
     */
    private static void setScrollPaneBehaviour(JScrollPane scrollPane, JPanel viewportPanel) {
        scrollPane.setViewportView(viewportPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
    }

    /**
     * Create an event group JPanel and set the panel layout parameters.
     * Get event panels using the EventComponents list and add them to a list.
     * Add the event panels to the event group panel.
     *
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param uniqueEventGroup          the unique event group
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     *
     * @return                          the populated event group JPanel
     */
    private static JPanel getEventGroupPanel(List<EventComponents> eventComponentsList, String uniqueEventGroup, List<String> specialtySubgroupList, UserPreferences userPrefs, Preferences windowsRegistry) {
        JPanel eventGroupPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        setEventGroupPanelLayout(layout);

        List<JPanel> eventsPanelList = new ArrayList<>();

        for (EventComponents event : eventComponentsList) {
            if (event.getGroup().equals(uniqueEventGroup)) {
                JPanel eventPanel = populateEventsPanel(eventComponentsList, specialtySubgroupList, event, userPrefs, windowsRegistry);
                eventsPanelList.add(eventPanel);
            }
        }

        for (JPanel eventsPanel : eventsPanelList) {
            if (eventsPanel.equals(eventsPanelList.getLast())) {
                layout.insets = new Insets(0, 0, 20, 0);
            }

            layout.gridy += 1;
            eventGroupPanel.add(eventsPanel, layout);
        }

        return eventGroupPanel;
    }

    /**
     * Set the event group panel anchor, fill, X/Y weights, and insets.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void setEventGroupPanelLayout(GridBagConstraints layout) {
        setPanelLayout(layout);
        layout.insets = new Insets(0, 0, 50, 0);
    }

    /**
     * Create an events JPanel and set the layout parameters.
     * Add a title checkbox, separator, and update the panel layout parameters.
     * Add the event checkboxes and location labels.
     *
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param event                     the individual EventComponents data
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     *
     * @return                          the populated events JPanel
     */
    private static JPanel populateEventsPanel(List<EventComponents> eventComponentsList, List<String> specialtySubgroupList, EventComponents event, UserPreferences userPrefs, Preferences windowsRegistry) {
        JPanel eventsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        setEventsPanelLayout(layout);

        int rowCount = 0;

        addTitleCheckbox(eventComponentsList, specialtySubgroupList, eventsPanel, event.getTitleCheckbox(), layout, rowCount, userPrefs, windowsRegistry);
        rowCount += 1;
        addSeparator(eventsPanel, layout, rowCount);
        rowCount += 1;

        updateEventsPanelLayout(layout);
        int startRow = rowCount;

        for (JCheckBox eventCheckbox : event.getEventCheckboxes()) {
            addEventCheckbox(eventComponentsList, specialtySubgroupList, eventsPanel, eventCheckbox, layout, rowCount, userPrefs, windowsRegistry);
            rowCount += 1;
        }
        for (JLabel locationLabel : event.getLocationLabels()) {
            addLocationLabel(eventsPanel, locationLabel, layout, startRow);
            startRow += 1;
        }

        return eventsPanel;
    }

    /**
     * Set the events panel anchor, grid X position, X weight, and insets.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void setEventsPanelLayout(GridBagConstraints layout) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.gridx = 0;
        layout.weightx = 1;
        layout.insets = new Insets(11, 25, -5, 15);
    }

    /**
     * Update the events panel insets and grid width.
     *
     * @param layout    the GridBagConstraints layout parameters class
     */
    private static void updateEventsPanelLayout(GridBagConstraints layout) {
        layout.insets = new Insets(20, 25, 0, 15);
        layout.gridwidth = 1;
    }

    /**
     * Set the panel fill, grid Y position, grid width, and insets.
     * Add a new JSeparator to the panel.
     *
     * @param panel         a JPanel component
     * @param layout        the GridBagConstraints layout parameters class
     * @param rowCount      the numerical representation of the grid Y position
     */
    private static void addSeparator(JPanel panel, GridBagConstraints layout, int rowCount) {
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridy = rowCount;
        layout.gridwidth = GridBagConstraints.REMAINDER;
        layout.insets = new Insets(15, 25, 0, 10);

        panel.add(new JSeparator(), layout);
    }

    /**
     * Set the events panel anchor, insets, grid Y position, and Y weight.
     * Add an item listener to the title checkbox and add the title checkbox to the events panel.
     *
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param eventsPanel               the events JPanel component
     * @param titleCheckbox             the subgroup title JCheckBox
     * @param layout                    the GridBagConstraints layout parameters class
     * @param rowCount                  the numerical representation of the grid Y position
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     */
    private static void addTitleCheckbox(List<EventComponents> eventComponentsList, List<String> specialtySubgroupList, JPanel eventsPanel, JCheckBox titleCheckbox, GridBagConstraints layout, int rowCount, UserPreferences userPrefs, Preferences windowsRegistry) {
        layout.anchor = GridBagConstraints.FIRST_LINE_START;
        layout.insets = new Insets(11, 25, -4, 15);
        layout.gridy = rowCount;
        layout.weighty = 0;

        addCheckboxListener(eventComponentsList, specialtySubgroupList, titleCheckbox, userPrefs, windowsRegistry);

        eventsPanel.add(titleCheckbox, layout);
    }

    /**
     * Add an item listener to get the checkbox text and name, update related checkboxes, and save the changed checkbox state.
     * Determine the notify state name based on the checkbox text, checkbox name, and specialty subgroup.
     * Save the notify states in the user preferences and Windows Registry.
     *
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param checkbox                  the individual JCheckBox
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     */
    private static void addCheckboxListener(List<EventComponents> eventComponentsList, List<String> specialtySubgroupList, JCheckBox checkbox, UserPreferences userPrefs, Preferences windowsRegistry) {
        checkbox.addItemListener(itemEventReceiver -> {
            String checkboxText = checkbox.getText();
            String checkboxName = checkbox.getName();

            if (!checkboxText.equals(checkboxName)) {
                if (specialtySubgroupList.contains(checkboxName)) {
                    checkboxText = checkboxName + " (" + checkboxText + ")";
                }
                else if (checkboxText.contains(checkboxName)) {
                    checkboxText = checkboxName;
                }
            }

            Pair<String, Boolean> notifyState = new MutablePair<>(checkboxText, checkbox.isSelected());

            updateEventCheckboxes(eventComponentsList, notifyState, userPrefs);
            setNotifyStates(notifyState, userPrefs);
            saveStatePreferences(userPrefs.getNotifyStates(), windowsRegistry);
        });
    }

    /**
     * Update the event checkbox states based on the notify state changed.
     *
     * @param eventComponentsList   a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param notifyState           a notify state pair
     * @param userPrefs             the user preferences with notification minutes and a list of notify states
     */
    private static void updateEventCheckboxes(List<EventComponents> eventComponentsList, Pair<String, Boolean> notifyState, UserPreferences userPrefs) {
        for (EventComponents event : eventComponentsList) {
            String titleCheckboxName = event.getTitleCheckbox().getName();

            for (JCheckBox eventCheckbox : event.getEventCheckboxes()) {
                if (eventCheckbox.getName().equals(titleCheckboxName) && notifyState.getKey().equals(titleCheckboxName)) {
                    eventCheckbox.setSelected(notifyState.getValue());
                    setNotifyStates(notifyState, userPrefs);
                }
            }
        }
    }

    /**
     * Replace the old notify state in the notify states list with the checkbox notify state pair.
     * Save the new notify states list in the user preferences.
     *
     * @param checkboxNotifyState   the notify state pair from a checkbox
     * @param userPrefs             the user preferences with notification minutes and a list of notify states
     */
    private static void setNotifyStates(Pair<String, Boolean> checkboxNotifyState, UserPreferences userPrefs) {
        List<Pair<String, Boolean>> notifyStates = userPrefs.getNotifyStates();

        for (Pair<String, Boolean> state : notifyStates) {
            if (state.getKey().equals(checkboxNotifyState.getKey())) {
                int indexOfEvent = notifyStates.indexOf(state);
                notifyStates.set(indexOfEvent, checkboxNotifyState);
            }
        }

        userPrefs.setNotifyStates(notifyStates);
    }

    /**
     * Save the notify states user preferences to the Windows Registry.
     *
     * @param notifyStates      the user preferences for notifications per event
     * @param windowsRegistry   the path to the Windows Registry data for the app
     */
    private static void saveStatePreferences(List<Pair<String, Boolean>> notifyStates, Preferences windowsRegistry) {
        for (Pair<String, Boolean> state : notifyStates) {
            windowsRegistry.putBoolean(state.getKey(), state.getValue());
        }
    }

    /**
     * Set the events panel grid Y position and Y weight.
     * Add an item listener to the event checkbox and add the event checkbox to the events panel.
     *
     * @param eventComponentsList       a list of event components with a group, title checkbox, event checkboxes, and location labels
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param eventsPanel               the events JPanel component
     * @param eventCheckbox             the individual event JCheckBox
     * @param layout                    the GridBagConstraints layout parameters class
     * @param rowCount                  the numerical representation of the grid Y position
     * @param userPrefs                 the user preferences with notification minutes and a list of notify states
     * @param windowsRegistry           the path to the Windows Registry data for the app
     */
    private static void addEventCheckbox(List<EventComponents> eventComponentsList, List<String> specialtySubgroupList, JPanel eventsPanel, JCheckBox eventCheckbox, GridBagConstraints layout, int rowCount, UserPreferences userPrefs, Preferences windowsRegistry) {
        layout.gridy = rowCount;
        layout.weighty = 0;

        addCheckboxListener(eventComponentsList, specialtySubgroupList, eventCheckbox, userPrefs, windowsRegistry);

        eventsPanel.add(eventCheckbox, layout);
    }

    /**
     * Set the events panel anchor, grid X and Y positions, and Y weight.
     * Add the location label to the events panel.
     *
     * @param eventsPanel       the events JPanel component
     * @param locationLabel     the individual location JLabel
     * @param layout            the GridBagConstraints layout parameters class
     * @param startRow          the numerical representation of the grid Y position
     */
    private static void addLocationLabel(JPanel eventsPanel, JLabel locationLabel, GridBagConstraints layout, int startRow) {
        layout.anchor = GridBagConstraints.EAST;
        layout.gridx = 1;
        layout.gridy = startRow;
        layout.weighty = 0;

        eventsPanel.add(locationLabel, layout);
    }

    /**
     * Create a new festival panel and set the layout parameters.
     * Add a title label, separator, and countdown label to the festival panel.
     *
     * @param festivalComponents    a title label and a countdown label for a festival
     * @return                      the populated festival JPanel
     */
    private static JPanel populateFestivalPanel(FestivalComponents festivalComponents) {
        JPanel festivalPanel = new JPanel(new GridBagLayout());
        GridBagConstraints layout = new GridBagConstraints();
        setPanelLayout(layout);

        int rowCount = 0;

        addTitleLabel(festivalPanel, festivalComponents.getTitleLabel(), layout, rowCount);
        rowCount += 1;
        addSeparator(festivalPanel, layout, rowCount);
        rowCount += 1;
        addCountdownLabel(festivalPanel, festivalComponents.getCountdownLabel(), layout, rowCount);

        return festivalPanel;
    }

    /**
     * Set the festival panel insets, grid Y position, and Y weight.
     * Add the title label to the festival panel.
     *
     * @param festivalPanel     the festival JPanel component
     * @param titleLabel        the festival title JLabel
     * @param layout            the GridBagConstraints layout parameters class
     * @param rowCount          the numerical representation of the grid Y position
     */
    private static void addTitleLabel(JPanel festivalPanel, JLabel titleLabel, GridBagConstraints layout, int rowCount) {
        layout.insets = new Insets(13, 25, -2, 15);
        layout.gridy = rowCount;
        layout.weighty = 0;

        festivalPanel.add(titleLabel, layout);
    }

    /**
     * Set the festival panel insets, grid Y position, and Y weight.
     * Add the countdown label to the festival panel.
     *
     * @param festivalPanel     the festival JPanel component
     * @param countdownLabel    the festival countdown JLabel
     * @param layout            the GridBagConstraints layout parameters class
     * @param rowCount          the numerical representation of the grid position
     */
    private static void addCountdownLabel(JPanel festivalPanel, JLabel countdownLabel, GridBagConstraints layout, int rowCount) {
        layout.insets = new Insets(22, 25, 0, 15);
        layout.gridy = rowCount;
        layout.weighty = 0;
        
        festivalPanel.add(countdownLabel, layout);
    }
}
