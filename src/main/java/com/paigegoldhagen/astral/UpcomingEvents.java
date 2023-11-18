package com.paigegoldhagen.astral;

/**
 * For setting and getting upcoming event data.
 */
public class UpcomingEvents {
	private final String Name, Location;
	
	public UpcomingEvents(String eventName, String eventLocation) {
		this.Name = eventName;
		this.Location = eventLocation;
	}
	
	public String getName() {return Name;}
	public String getLocation() {return Location;}
}