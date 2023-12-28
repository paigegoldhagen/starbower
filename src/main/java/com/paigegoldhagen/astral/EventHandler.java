package com.paigegoldhagen.astral;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * For creating a dynamic list of GUI components for events.
 */
public class EventHandler {
    /**
     * Create a new EventComponents class instance per unique event subgroup and add it to the EventComponents list.
     *
     * @param eventList                 a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param lastEventGroup            the last event group name
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param notifyStates              the user preferences for notifications per event
     *
     * @return                          a list of populated EventComponents class instances
     */
    public static List<EventComponents> getEventComponentsList(List<Event> eventList, String lastEventGroup, List<String> uniqueEventSubgroups, List<String> specialtySubgroupList, List<Pair<String, Boolean>> notifyStates) {
        List<EventComponents> eventComponentsList = new ArrayList<>();

        for (String eventSubgroup : uniqueEventSubgroups) {
            EventComponents eventComponents = getEventComponents(eventList, lastEventGroup, eventSubgroup, specialtySubgroupList, notifyStates);

            if (eventComponents.getGroup() != null) {
                eventComponentsList.add(eventComponents);
            }
        }

        return eventComponentsList;
    }

    /**
     * Create a list of event checkboxes and location labels, a title checkbox,
     * and use the components to create a new EventComponents class instance.
     *
     * @param eventList                 a list of events with a group, subgroup, type, name, location, waypoint, inital time, frequency, and (optional) schedule
     * @param lastEventGroup            the last event group name
     * @param uniqueEventSubgroup       a list of unique event subgroup names
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param notifyStates              the user preferences for notifications per event
     *
     * @return                          a new EventComponents instance with a group string, title checkbox, list of event checkboxes, and a list of location labels
     */
    private static EventComponents getEventComponents(List<Event> eventList, String lastEventGroup, String uniqueEventSubgroup, List<String> specialtySubgroupList, List<Pair<String, Boolean>> notifyStates) {
        String group = null;
        JCheckBox titleCheckbox = null;
        List<JCheckBox> eventCheckboxes = new ArrayList<>();
        List<JLabel> locationLabels = new ArrayList<>();

        String subgroupTitle = null;

        for (Pair<String, Boolean> state : notifyStates) {
            String stateName = state.getKey();
            boolean isStateEnabled = state.getValue();

            for (Event event : eventList) {
                String eventGroup = event.getGroup();
                String eventSubgroup = event.getSubgroup();

                if (eventSubgroup.equals(uniqueEventSubgroup)) {
                    populateEventComponents(event, uniqueEventSubgroup, specialtySubgroupList, stateName, isStateEnabled, eventCheckboxes, locationLabels);

                    if (group == null) {
                        group = eventGroup;
                    }
                }

                if (eventGroup.equals(lastEventGroup) && specialtySubgroupList.contains(eventSubgroup)) {
                    subgroupTitle = eventSubgroup + ": " + event.getName();
                }
            }

            if (stateName.equals(uniqueEventSubgroup)) {
                titleCheckbox = createTitleCheckbox(stateName, uniqueEventSubgroup, isStateEnabled);
            }
            else if (subgroupTitle != null && subgroupTitle.contains(stateName) && !subgroupTitle.equals(stateName)) {
                titleCheckbox = createTitleCheckbox(subgroupTitle, uniqueEventSubgroup, isStateEnabled);
            }
        }

        return new EventComponents(group, titleCheckbox, eventCheckboxes, locationLabels);
    }

    /**
     * Determine the event checkbox text and location label text based on the subgroup and specialty subgroup.
     *
     * @param event                     the individual event data
     * @param uniqueEventSubgroup       the unique subgroup name
     * @param specialtySubgroupList     a list of specialty subgroup names
     * @param stateName                 the name of the event from user preferences
     * @param isStateEnabled            the state of the event from user preferences
     * @param eventCheckboxes           a list of JCheckboxes
     * @param locationLabels            a list of JLabels
     */
    private static void populateEventComponents(Event event, String uniqueEventSubgroup, List<String> specialtySubgroupList, String stateName, Boolean isStateEnabled, List<JCheckBox> eventCheckboxes, List<JLabel> locationLabels) {
        String eventName = event.getName();
        String eventLocation = event.getLocation();

        if (eventName.equals(stateName) && !eventName.equals(uniqueEventSubgroup)) {
            addEventComponents(eventName, uniqueEventSubgroup, isStateEnabled, eventLocation, eventCheckboxes, locationLabels);
        }
        else if (specialtySubgroupList.contains(uniqueEventSubgroup) && stateName.contains(eventName) && stateName.contains(eventLocation) && !stateName.equals(eventName)) {
            addEventComponents(eventLocation, uniqueEventSubgroup, isStateEnabled, event.getWaypoint(), eventCheckboxes, locationLabels);
        }
    }

    /**
     * Create an event checkbox and a location label, and add each component to the respective component list.
     *
     * @param eventName             the individual event name
     * @param uniqueEventSubgroup   the unique subgroup name
     * @param isStateEnabled        the state of the event from user preferences
     * @param eventLocation         the individual event location name
     * @param eventCheckboxes       a list of JCheckboxes
     * @param locationLabels        a list of JLabels
     */
    private static void addEventComponents(String eventName, String uniqueEventSubgroup, Boolean isStateEnabled, String eventLocation, List<JCheckBox> eventCheckboxes, List<JLabel> locationLabels) {
        JCheckBox eventCheckbox = createEventCheckbox(eventName, uniqueEventSubgroup, isStateEnabled);
        eventCheckboxes.add(eventCheckbox);

        JLabel locationLabel = createLocationLabel(eventLocation);
        locationLabels.add(locationLabel);
    }

    /**
     * Create a title checkbox and set the text style.
     *
     * @param subgroupTitle     the string to set as the checkbox text
     * @param eventSubgroup     the string to set as the checkbox name
     * @param isStateEnabled    the boolean state of the checkbox
     *
     * @return                  a new title JCheckbox
     */
    private static JCheckBox createTitleCheckbox(String subgroupTitle, String eventSubgroup, Boolean isStateEnabled) {
        JCheckBox titleCheckbox = new JCheckBox(subgroupTitle, isStateEnabled);
        titleCheckbox.setName(eventSubgroup);
        titleCheckbox.putClientProperty("FlatLaf.styleClass", "large");
        return titleCheckbox;
    }

    /**
     * Create an event checkbox.
     *
     * @param eventName         the string to set as the checkbox text
     * @param eventSubgroup     the string to set as the checkbox name
     * @param isStateEnabled    the boolean state of the checkbox
     *
     * @return                  a new event JCheckbox
     */
    private static JCheckBox createEventCheckbox(String eventName, String eventSubgroup, Boolean isStateEnabled) {
        JCheckBox eventCheckbox = new JCheckBox(eventName, isStateEnabled);
        eventCheckbox.setName(eventSubgroup);
        return eventCheckbox;
    }

    /**
     * Create a location label and set the horizontal alignment of the text.
     *
     * @param eventLocation     the string to set as the label text
     * @return                  a new location JLabel
     */
    private static JLabel createLocationLabel(String eventLocation) {
        JLabel locationLabel = new JLabel(eventLocation);
        locationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        return locationLabel;
    }
}
