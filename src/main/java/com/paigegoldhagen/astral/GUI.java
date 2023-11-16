package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The main loop and the graphical user interface for the app.
 */
public class GUI {
    private static String timeChoice;
    private static List<String> eventChoices;

    /**
     * Prepare the tray icon, get the event data and the default/saved user preferences,
     * display the GUI, and send a notification if there is an upcoming event.
     * Pause for 1 second or 1 minute at the end of every loop.
     *
     * @throws InterruptedException     the current thread was interrupted
     * @throws IOException              the file couldn't be found or read
     * @throws AWTException             an error occurred with the GUI components or window frame
     */
    public static void main(String[] args) throws InterruptedException, IOException, AWTException {
        BufferedImage appIcon = FileHandler.loadImage("icon.png");
        TrayIcon trayIcon = TrayHandler.prepareTrayIcon(appIcon);

        InputStream csvFile = FileHandler.getInputStream("event-data.csv");
        List<Events> eventsList = FormatHandler.mapCsvToBeans(csvFile);
        List<Object[]> eventsTimetable = EventsHandler.getEventsTimetable(eventsList);

        timeChoice = PreferenceHandler.getTimePreference();
        String defaultEventPreferences = FormatHandler.calculateDefaultEventPreferences(eventsTimetable);
        String eventPreferences = PreferenceHandler.getEventPreferences(defaultEventPreferences);
        eventChoices = FormatHandler.getEventPreferencesAsList(eventPreferences);

        List<Object[]> checkboxParameters = FormatHandler.getCheckboxParameters(eventsTimetable, eventChoices);
        initialiseGUI(checkboxParameters);

        while (true) {
            PreferenceHandler.setTimePreference(timeChoice);
            String eventPreferencesString = FormatHandler.getEventPreferencesAsString(eventChoices);
            PreferenceHandler.setEventPreferences(eventPreferencesString);

            LocalTime utcTime = TimeHandler.getUtcTime();
            long millisecondsToWait = 1000;

            List<Object[]> upcomingEvents = EventsHandler.getUpcomingEvents(eventsTimetable, timeChoice, eventChoices, utcTime);

            if (!upcomingEvents.isEmpty()) {
                String notificationMessage = FormatHandler.getNotificationMessage(upcomingEvents, timeChoice);
                Notifications.sendNotification(trayIcon, notificationMessage);
                millisecondsToWait = 60000;
            }
            Thread.sleep(millisecondsToWait);
        }
    }

    /**
     * Display the GUI components and add GUI component listeners for user input.
     *
     * @param checkboxParameters        the list of parameters to initialise the checkbox contents
     */
    private static void initialiseGUI(List<Object[]> checkboxParameters) {
        Frame frame = new Frame();
        Panel panel = new Panel();

        List<Checkbox> checkboxComponents = addCheckboxComponents(panel, checkboxParameters);

        Component[] guiComponents = loadGUIComponents(panel);
        Choice dropdown = (Choice) guiComponents[3];

        String[] timeChoices = new String[]{"5", "10", "15", "20", "30", "60"};

        for (String time : timeChoices) {
            dropdown.add(time);
        }

        for (Component comp : guiComponents) {
            frame.add(comp);
        }

        frame.setSize(380, 400);
        frame.setTitle("Astral");
        frame.setLayout(null);
        frame.setVisible(true);

        dropdown.select(timeChoice);

        addComponentListeners(frame, dropdown, checkboxComponents);
    }

    /**
     * Set the contents for each checkbox and add the checkbox components to a list.
     *
     * @param panel                 the container for holding GUI components
     * @param checkboxParameters    the list of parameters to initialise the checkbox contents
     *
     * @return                      a list of the checkbox components
     */
    private static List<Checkbox> addCheckboxComponents(Panel panel, List<Object[]> checkboxParameters) {
        List<Checkbox> checkboxComponents = new ArrayList<>();

        for (Object[] param : checkboxParameters) {
            String name = (String) param[0];
            boolean checkboxState = (boolean) param[1];

            Checkbox checkbox = new Checkbox(name, checkboxState);
            panel.add(checkbox);
            checkboxComponents.add(checkbox);
        }
        return checkboxComponents;
    }

    /**
     * Add a window listener to the frame to close the program
     * and add item listeners to the dropdown and checkbox components.
     *
     * @param frame                     the visual window that the GUI components appear on
     * @param dropdown                  the dropdown list that a user can choose from
     * @param checkboxComponents        the list of checkboxes that a user can check or uncheck
     */
    private static void addComponentListeners(Frame frame, Choice dropdown, List<Checkbox> checkboxComponents) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        dropdown.addItemListener(e -> {
            timeChoice = (String) e.getItem();
        });

        for (Checkbox checkbox : checkboxComponents) {
            checkbox.addItemListener(e -> {
                int indexOfEvent = checkboxComponents.indexOf(checkbox);
                int eventChoiceSelected = e.getStateChange();
                String eventChoiceString = String.valueOf(eventChoiceSelected);

                eventChoices.set(indexOfEvent, eventChoiceString);
            });
        }
    }

    /**
     * Set the contents and visual bounds for each GUI component.
     *
     * @param panel     the container for holding GUI components
     * @return          a component list of the GUI components
     */
    private static Component[] loadGUIComponents(Panel panel) {
        Label subtitle = new Label("GW2 Event Notifications");
        Label leftText = new Label("Send a reminder");
        Label rightText = new Label("minutes before the next world boss");
        Choice dropdown = new Choice();
        Label scrollWindowText = new Label("Enable notifications per event:");

        panel.setLayout(new GridLayout(0, 1));
        ScrollPane scrollWindow = new ScrollPane();
        scrollWindow.add(panel);
        scrollWindow.setSize(panel.getSize());

        subtitle.setBounds(15, 45, 300, 30);
        leftText.setBounds(15, 90, 100, 20);
        rightText.setBounds(175, 90, 300, 20);
        dropdown.setBounds(115, 90, 50, 50);
        scrollWindowText.setBounds(15, 140, 300, 30);
        scrollWindow.setBounds(15, 170, 350, 200);

        return new Component[]{subtitle, leftText, rightText, dropdown, scrollWindowText, scrollWindow};
    }
}
