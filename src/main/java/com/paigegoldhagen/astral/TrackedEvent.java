package com.paigegoldhagen.astral;

/**
 * For setting and getting tracked event strings.
 */
public class TrackedEvent {
    private final String Type, Name;

    public TrackedEvent(String type, String name) {
        this.Type = type;
        this.Name = name;
    }

    public String getType() {return Type;}
    public String getName() {return Name;}
}
