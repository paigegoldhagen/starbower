package com.paigegoldhagen.astral;

/**
 * For mapping event data and getting event data variables.
 */
public class Event {
    public String Group, Subgroup, Type, Name, Location, Waypoint, Time;
    public Integer Frequency;
    public String[] Schedule;

    public String getGroup() {return Group;}
    public String getSubgroup() {return Subgroup;}
    public String getType() {return Type;}
    public String getName() {return Name;}
    public String getLocation() {return Location;}
    public String getWaypoint() {return Waypoint;}
    public String getTime() {return Time;}
    public Integer getFrequency() {return Frequency;}
    public String[] getSchedule() {return Schedule;}
}
