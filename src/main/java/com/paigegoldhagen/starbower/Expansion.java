package com.paigegoldhagen.starbower;

/**
 * Setting and getting Expansion information.
 */
public class Expansion {
    public Integer ID;
    public String Name;

    public Expansion(Integer id, String name) {
        this.ID = id;
        this.Name = name;
    }

    public Integer getID() {return ID;}
    public String getName() {return Name;}
}