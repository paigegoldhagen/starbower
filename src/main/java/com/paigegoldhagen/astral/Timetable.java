package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.util.List;

/**
 * For setting and getting event timetable data.
 */
public class Timetable {
	private final String Name, Location;
	private final List<LocalTime> Times;
	
	public Timetable(String eventName, String eventLocation, List<LocalTime> eventTimes) {
		this.Name = eventName;
		this.Location = eventLocation;
		this.Times = eventTimes;
	}
	
	public String getName() {return Name;}
	public String getLocation() {return Location;}
	public List<LocalTime> getTimes() {return Times;}
}