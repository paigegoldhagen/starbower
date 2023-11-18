package com.paigegoldhagen.astral;

/**
 * The structure for mapping the data of a CSV file to a list of beans.
 */
public class Events {
    public String Name, Location, Time;
    public Integer Frequency;
    public String[] Schedule;

    public String getName() {return Name;}
    public String getLocation() {return Location;}
    public String getTime() {return Time;}
    public Integer getFrequency() {return Frequency;}
    public String[] getSchedule() {return Schedule;}
}
