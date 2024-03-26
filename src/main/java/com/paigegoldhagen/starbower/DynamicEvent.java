package com.paigegoldhagen.starbower;

/**
 * Setting and getting DynamicEvent information.
 */
public class DynamicEvent {
    public String Name, WaypointName, MapName;
    public Integer NotifyStateID, KindID;
    public Boolean NotifyStateEnabled;

    public DynamicEvent(String name, Integer kindID, String mapName) {
        this.Name = name;
        this.KindID = kindID;
        this.MapName = mapName;
    }

    public DynamicEvent(String name, Integer notifyStateID, Boolean notifyStateEnabled, String mapName, String waypointName) {
        this.Name = name;
        this.NotifyStateID = notifyStateID;
        this.NotifyStateEnabled = notifyStateEnabled;
        this.MapName = mapName;
        this.WaypointName = waypointName;
    }

    public String getName() {return Name;}
    public Integer getNotifyStateID() {return NotifyStateID;}
    public Boolean getNotifyStateEnabled() {return NotifyStateEnabled;}
    public Integer getKindID() {return KindID;}
    public String getMapName() {return MapName;}
    public String getWaypointName() {return WaypointName;}
}