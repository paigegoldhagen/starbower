package com.paigegoldhagen.astral;

import java.awt.*;
import java.io.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The entry point for the app.
 */
public class Astral {
	/**
	 * Prepare the tray icon, get the events timetable and default user preferences,
	 * display the GUI, and send a notification if there is an upcoming event.
	 * Pause for 1 second or 1 minute at the end of every loop.
	 *
	 * @throws InterruptedException			the current thread was interrupted
	 * @throws IOException					the file couldn't be found or read
	 * @throws AWTException					an error occurred with the GUI components or window frame
	 */
	public static void main(String[] args) throws InterruptedException, IOException, AWTException {
		TrayIcon trayIcon = getTrayIcon();
		
		List<Timetable> timetable = getTimetable();
		String defaultPreferences = calculateDefaultPreferences(timetable);
		
		GUI.initialiseGUI(defaultPreferences, timetable);
		
		while (true) {
			String notifyTime = GUI.getNotifyTime();
			String notifyState = GUI.getNotifyState(defaultPreferences);
			
			LocalTime utcTime = TimeHandler.getUtcTime();
			long millisecondsToWait = 1000;
			
			List<UpcomingEvents> upcomingEvents = EventsHandler.getUpcomingEvents(timetable, notifyTime, notifyState, utcTime);
			
			if (!upcomingEvents.isEmpty()) {
				String message = FormatHandler.getNotificationMessage(upcomingEvents, notifyTime);
				Notifications.sendNotification(trayIcon, message);
				millisecondsToWait = 60000;
			}
			Thread.sleep(millisecondsToWait);
		}
	}

	/**
	 * Prepare a tray icon using the PNG file image.
	 *
	 * @return					the prepared TrayIcon
	 *
	 * @throws IOException		the file couldn't be found or read
	 * @throws AWTException		an error occurred with the GUI components or window frame
	 */
	private static TrayIcon getTrayIcon() throws IOException, AWTException {
		Image appIcon = FileHandler.loadImage("icon.png");
		return TrayHandler.prepareTrayIcon(appIcon);
	}

	/**
	 * Get the events list from the CSV file input stream.
	 *
	 * @return		the Events class as a list of beans
	 */
	private static List<Events> getEventsList() {
		InputStream csvFile = FileHandler.getInputStream("event-data.csv");
		return FormatHandler.mapCsvToBeans(csvFile);
	}

	/**
	 * Calculate projected event times and add the updated events to the Timetable class list.
	 *
	 * @return		a list of events with name, location, and projected event times
	 */
	private static List<Timetable> getTimetable() {
		List<Events> eventsList = getEventsList();
		List<Timetable> timetable = new ArrayList<>();
		
		for (Events e : eventsList) {
			String name = e.getName();
			String location = e.getLocation();
			List<LocalTime> times = EventsHandler.getProjectedEventTimes(e);
			
			Timetable event = new Timetable(name, location, times);
			timetable.add(event);
		}
		return timetable;
	}

	/**
	 * Calculate the default state preferences based on the number of individual events.
	 *
	 * @param timetable		the list of events with name, location, and projected event times
	 * @return				a string containing the default state preferences
	 */
	private static String calculateDefaultPreferences(List<Timetable> timetable) {
		StringBuilder defaultPreferences = new StringBuilder();
		
		for (Timetable t : timetable) {
			defaultPreferences.append("1");
		}
		return String.valueOf(defaultPreferences);
	}
}