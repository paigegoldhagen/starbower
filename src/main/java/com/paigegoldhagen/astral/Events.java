package com.paigegoldhagen.astral;

/**
 * The structure for mapping the data of a CSV file
 * to a list of beans using OpenCSV - CsvToBeanBuilder.
 */
public class Events {
    public String Name, Time;
    public Integer Frequency;

    public String getName() { return Name; }
    public String getTime() { return Time; }
    public Integer getFrequency() { return Frequency; }
}
