package com.paigegoldhagen.astral;

import javax.swing.*;
import java.util.List;

/**
 * For setting and getting GUI components for events.
 */
public class EventComponents {
    private final String Group;
    private final JCheckBox TitleCheckbox;
    private final List<JCheckBox> EventCheckboxes;
    private final List<JLabel> LocationLabels;

    public EventComponents(String group, JCheckBox titleCheckbox, List<JCheckBox> eventCheckboxes, List<JLabel> locationLabels) {
        this.Group = group;
        this.TitleCheckbox = titleCheckbox;
        this.EventCheckboxes = eventCheckboxes;
        this.LocationLabels = locationLabels;
    }

    public String getGroup() {return Group;}
    public JCheckBox getTitleCheckbox() {return TitleCheckbox;}
    public List<JCheckBox> getEventCheckboxes() {return EventCheckboxes;}
    public List<JLabel> getLocationLabels() {return LocationLabels;}
}
