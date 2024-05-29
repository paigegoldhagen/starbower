package com.paigegoldhagen.starbower;

/**
 * Setting and getting Dropdown data using OpenCSV mapping.
 */
public class Dropdown {
    public String FirstLabelText, SecondLabelText, PreferenceKey, PreferenceValue;
    public String[] SelectionData;
    public Integer SizeWidth, SizeHeight;

    public String getFirstLabelText() {return FirstLabelText;}
    public String getSecondLabelText() {return SecondLabelText;}
    public String getPreferenceKey() {return PreferenceKey;}
    public String getPreferenceValue() {return PreferenceValue;}
    public String[] getSelectionData() {return SelectionData;}
    public Integer getSizeWidth() {return SizeWidth;}
    public Integer getSizeHeight() {return SizeHeight;}
}