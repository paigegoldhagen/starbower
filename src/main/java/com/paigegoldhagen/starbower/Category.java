package com.paigegoldhagen.starbower;

/**
 * Setting and getting Category information.
 */
public class Category {
    public Integer ID, NotifyStateID, ExpansionID;
    public String Name;
    public Boolean NotifyStateEnabled;

    public Category(Integer id, String name) {
        this.ID = id;
        this.Name = name;
    }

    public Category(Integer id, Integer expansionID) {
        this.ID = id;
        this.ExpansionID = expansionID;
    }

    public Category(Integer id, String name, Integer notifyStateID, Boolean notifyStateEnabled) {
        this.ID = id;
        this.Name = name;
        this.NotifyStateID = notifyStateID;
        this.NotifyStateEnabled = notifyStateEnabled;
    }

    public Integer getID() {return ID;}
    public String getName() {return Name;}
    public Integer getNotifyStateID() {return NotifyStateID;}
    public Boolean getNotifyStateEnabled() {return NotifyStateEnabled;}
    public Integer getExpansionID() {return ExpansionID;}
}