package com.paigegoldhagen.astral;

import java.util.List;
import java.time.LocalTime;

/**
 * For setting and getting event timetable variables.
 */
public class EventTimetable {
    private final String Subgroup, Type, Name, Location;
    private final List<LocalTime> Times;

    public EventTimetable(String subgroup, String type, String name, String location, List<LocalTime> times) {
        this.Subgroup = subgroup;
        this.Type = type;
        this.Name = name;
        this.Location = location;
        this.Times = times;
    }

    public String getSubgroup() {return Subgroup;}
    public String getType() {return Type;}
    public String getName() {return Name;}
    public String getLocation() {return Location;}
    public List<LocalTime> getTimes() {return Times;}
}
