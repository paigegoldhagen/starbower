package com.paigegoldhagen.starbower;

/**
 * Setting and getting NotifyState information.
 */
public class NotifyState {
    public Integer ID;
    public Boolean IsEnabled;

    public NotifyState(Integer id, Boolean isEnabled) {
        this.ID = id;
        this.IsEnabled = isEnabled;
    }

    public Integer getID() {return ID;}
    public Boolean getIsEnabled() {return IsEnabled;}
}