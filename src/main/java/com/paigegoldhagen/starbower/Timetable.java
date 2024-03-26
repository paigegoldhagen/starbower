package com.paigegoldhagen.starbower;

import java.time.LocalTime;
import java.util.List;

/**
 * Setting and getting Timetable information for DynamicEvents.
 */
public class Timetable {
    public Integer DynamicEventID;
    public List<LocalTime> TimeList;

    public Timetable(Integer dynamicEventID, List<LocalTime> timeList) {
        this.DynamicEventID = dynamicEventID;
        this.TimeList = timeList;
    }

    public Integer getDynamicEventID() {return DynamicEventID;}
    public List<LocalTime> getTimeList() {return TimeList;}
}