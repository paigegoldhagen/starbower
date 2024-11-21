package com.paigegoldhagen.starbower;

/**
 * Setting and getting DynamicEvent information.
 */
public class DynamicEvent {
    public String Name, KindMessage, MapName, WaypointName, WaypointLink;
    public Integer NotifyStateID, KindID;
    public Boolean NotifyStateEnabled;

    public DynamicEvent(String name, Integer kindID, String kindMessage, String mapName) {
        this.Name = name;
        this.KindID = kindID;
        this.KindMessage = kindMessage;
        this.MapName = mapName;
    }

    public DynamicEvent(String name, Integer notifyStateID, Boolean notifyStateEnabled, String mapName, String waypointName, String waypointLink) {
        this.Name = name;
        this.NotifyStateID = notifyStateID;
        this.NotifyStateEnabled = notifyStateEnabled;
        this.MapName = mapName;
        this.WaypointName = waypointName;
        this.WaypointLink = waypointLink;
    }

    public String getName() {return Name;}
    public Integer getNotifyStateID() {return NotifyStateID;}
    public Boolean getNotifyStateEnabled() {return NotifyStateEnabled;}
    public Integer getKindID() {return KindID;}
    public String getKindMessage() {return KindMessage;}
    public String getMapName() {return MapName;}
    public String getWaypointName() {return WaypointName;}
    public String getWaypointLink() {return WaypointLink;}
}