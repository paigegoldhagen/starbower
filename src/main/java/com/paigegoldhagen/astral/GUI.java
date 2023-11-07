package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The graphical user interface for the app.
 */
public class GUI {
    private static Integer userChoice;
    /**
     * Initialise the GUI components in the frame, set the default or saved user choice,
     * and add listeners for user input.
     *
     * @param userPreference    the default or saved user choice
     */
    public static void displayGUI(String userPreference) {
        Frame frame = new Frame();

        Component[] guiComponents = loadGUIComponents();
        Choice dropdown = (Choice) guiComponents[3];

        String[] durationChoices = new String[]{"5", "10", "15", "20", "30", "60"};

        for (String duration : durationChoices) {
            dropdown.add(duration);
        }

        for (Component comp : guiComponents) {
            frame.add(comp);
        }

        frame.setSize(380, 180);
        frame.setTitle("Astral");
        frame.setLayout(null);
        frame.setVisible(true);

        dropdown.select(userPreference);
        userChoice = Integer.parseInt(userPreference);

        componentListeners(frame, dropdown);
    }

    /**
     * Get the default or saved user choice.
     *
     * @return      the user choice as an integer
     */
    public static Integer getUserChoice() {
        return userChoice;
    }

    /**
     * Add an action listener to the dropdown component to get the user choice,
     * and add a window listener to the frame to close the program.
     *
     * @param frame         the visual window that all GUI components appear on
     * @param dropdown      the dropdown list of durations that a user can choose from
     */
    private static void componentListeners(Frame frame, Choice dropdown) {
        dropdown.addItemListener(e -> {
            String dropdownChoice = (String) e.getItem();
            userChoice = Integer.parseInt(dropdownChoice);
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Set the contents and visual bounds for each GUI component.
     *
     * @return      a component list of the GUI components
     */
    private static Component[] loadGUIComponents() {
        Label subtitle = new Label("GW2 Event Notifications");
        Label leftText = new Label("Send a reminder");
        Label rightText = new Label("minutes before the next world boss");
        Choice dropdown = new Choice();

        subtitle.setBounds(15, 55, 300, 30);
        leftText.setBounds(15, 100, 100, 20);
        rightText.setBounds(175, 100, 300, 20);
        dropdown.setBounds(115,100,50,50);

        return new Component[]{subtitle, leftText, rightText, dropdown};
    }
}
