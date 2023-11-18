package com.paigegoldhagen.astral;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * The graphical user interface for the app.
 */
public class GUI {
	private static Preferences prefs;

	/**
	 * Display the GUI components and add GUI component listeners for user input.
	 *
	 * @param defaultPreferences		the default state preferences
	 * @param timetable					the list of events with name, location, and projected event times
	 */
	public static void initialiseGUI(String defaultPreferences, List<Timetable> timetable) {
        prefs = Preferences.userNodeForPackage(GUI.class);
        String notifyTime = getNotifyTime();
		String notifyState = getNotifyState(defaultPreferences);

		Frame frame = new Frame();
        Panel panel = new Panel();
		
		GUIComponents comp = loadGUIComponents(panel, timetable, notifyState);
		Choice dropdown = comp.getDropdown();

        String[] timeChoices = new String[]{"5", "10", "15", "20", "30", "60"};
        for (String time : timeChoices) {
            dropdown.add(time);
        }

		Component[] componentsList = comp.getComponentsList();
        for (Component c : componentsList) {
            frame.add(c);
        }

        frame.setSize(380, 400);
        frame.setTitle("Astral");
        frame.setLayout(null);
        frame.setVisible(true);

        dropdown.select(notifyTime);

        addComponentListeners(frame, dropdown, comp.getCheckboxes(), notifyState);
	}

	/**
	 * Add a window listener to the frame to close the program
	 * and add item listeners to the dropdown and checkbox components.
	 *
	 * @param frame				the visual window that the GUI components appear on
	 * @param dropdown			the dropdown list that a user can choose from
	 * @param checkboxes		the list of checkboxes that a user can check or uncheck
	 * @param notifyState		the checkbox state preferences
	 */
	private static void addComponentListeners(Frame frame, Choice dropdown, List<Checkbox> checkboxes, String notifyState) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });

        dropdown.addItemListener(e -> {
            String notifyTime = String.valueOf(e.getItem());
			prefs.put("TIME", notifyTime);
        });

		List<String> stateList = getStateList(notifyState);
        for (Checkbox c : checkboxes) {
            c.addItemListener(e -> {
				String state = String.valueOf(e.getStateChange());
                int indexOfEvent = checkboxes.indexOf(c);
				stateList.set(indexOfEvent, state);
				
				String stateString = getStateString(stateList);
				prefs.put("STATE", stateString);
            });
        }
    }

	/**
	 * Set the contents and visual bounds for each GUI component
	 * and add them to the GUIComponents class.
	 *
	 * @param panel					the container for holding GUI components
	 * @param timetable		the list of events with name, location, and projected event times
	 * @param notifyState			the checkbox state preferences
	 *
	 * @return						the GUIComponents class with the prepared GUI components
	 */
    private static GUIComponents loadGUIComponents(Panel panel, List<Timetable> timetable, String notifyState) {
        Label title = new Label("GW2 Event Notifications");
		Label subtitle = new Label("Enable notifications per event:");
        Label leftText = new Label("Send a reminder");
        Label rightText = new Label("minutes before the next world boss");
        Choice dropdown = new Choice();
		
		List<Checkbox> checkboxes = loadCheckboxComponents(panel, timetable, notifyState);

        panel.setLayout(new GridLayout(0, 1));
        ScrollPane scrollWindow = new ScrollPane();
        scrollWindow.add(panel);
        scrollWindow.setSize(panel.getSize());

        title.setBounds(15, 45, 300, 30);
		subtitle.setBounds(15, 140, 300, 30);
        leftText.setBounds(15, 90, 100, 20);
        rightText.setBounds(175, 90, 300, 20);
        dropdown.setBounds(115, 90, 50, 50);
        scrollWindow.setBounds(15, 170, 350, 200);

        return new GUIComponents(title, subtitle, leftText, rightText, dropdown, scrollWindow, checkboxes);
    }

	/**
	 * Set the contents for each checkbox and add the checkbox components to a list.
	 *
	 * @param panel					the container for holding GUI components
	 * @param timetable				the list of events with name, location, and projected event times
	 * @param notifyState			the checkbox state preferences
	 *
	 * @return						a list of the checkbox components
	 */
	private static List<Checkbox> loadCheckboxComponents(Panel panel, List<Timetable> timetable, String notifyState) {
		List<Parameters> checkboxParams = getCheckboxParameters(timetable, notifyState);
		List<Checkbox> checkboxComponents = new ArrayList<>();
		
		for (Parameters p : checkboxParams) {
			Checkbox checkbox = new Checkbox(p.getName(), p.getState());
			panel.add(checkbox);
			checkboxComponents.add(checkbox);
		}
		return checkboxComponents;
	}

	/**
	 * Get the name string and boolean state used for initialising checkbox contents
	 * based on the event name and whether notifications are enabled.
	 *
	 * @param timetable				the list of events with name, location, and projected event times
	 * @param notifyState			the checkbox state preferences
	 * @return						a list of checkbox parameters with the event name and notification state
	 */
	private static List<Parameters> getCheckboxParameters(List<Timetable> timetable, String notifyState) {
		List<String> stateList = getStateList(notifyState);
		List<Parameters> checkboxParams = new ArrayList<>();

		for (Timetable event : timetable) {
			String eventName = event.getName();
			String eventLocation = event.getLocation();
			
			int indexOfEvent = timetable.indexOf(event);
			String state = stateList.get(indexOfEvent);
			boolean checkboxState = state.equals("1");
			
			if (eventName.equals("Ley-Line Anomaly")) {
				eventName = eventName + " (" + eventLocation + ")";
			}
			
			Parameters eventParameters = new Parameters(eventName, checkboxState);
			checkboxParams.add(eventParameters);
		}
		return checkboxParams;
    }

	/**
	 * Separate each character in the state preferences string and add it to a list.
	 *
	 * @param notifyState		the state preferences string
	 * @return					a list of state preferences
	 */
	private static List<String> getStateList(String notifyState) {
		List<String> stateList = new ArrayList<>();
		for (int i = 0; i < notifyState.length(); i++) {
			String state = String.valueOf(notifyState.charAt(i));
			stateList.add(state);
		}
		return stateList;
	}

	/**
	 * Put all checkbox states into one string.
	 *
	 * @param stateList		the checkbox state list
	 * @return				a string of state preferences
	 */
	private static String getStateString(List<String> stateList) {
		StringBuilder notifyState = new StringBuilder();
		
		for (String s : stateList) {
			notifyState.append(s);
		}
		return String.valueOf(notifyState);
	}

	/**
	 * Get the default or saved time preference from the Windows Registry.
	 *
	 * @return		the time preference string
	 */
	public static String getNotifyTime() {
		return prefs.get("TIME", "10");
	}

	/**
	 * Get the default or saved state preferences from the Windows Registry.
	 *
	 * @param defaultPreferences		the default preferences based on the number of events
	 * @return							the state preferences string
	 */
	public static String getNotifyState(String defaultPreferences) {
		return prefs.get("STATE", defaultPreferences);
	}
}
