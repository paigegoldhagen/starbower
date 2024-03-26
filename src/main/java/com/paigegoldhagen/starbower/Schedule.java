package com.paigegoldhagen.starbower;

import java.time.LocalTime;

/**
 * Setting and getting Schedule information.
 */
public class Schedule {
    public LocalTime Time;
    public Integer Frequency;

    public Schedule(LocalTime time, Integer frequency) {
        this.Time = time;
        this.Frequency = frequency;
    }

    public LocalTime getTime() {return Time;}
    public Integer getFrequency() {return Frequency;}
}