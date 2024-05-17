package com.paigegoldhagen.starbower;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Setting and getting Festival information.
 */
public class Festival {
    public Integer CategoryID;
    public String Name;
    public LocalDateTime StartDate, EndDate;
    public Boolean IsOngoing;
    public List<DynamicEvent> DynamicEventList;

    public Festival(Integer categoryID, LocalDateTime startDate, LocalDateTime endDate) {
        this.CategoryID = categoryID;
        this.StartDate = startDate;
        this.EndDate = endDate;
    }

    public Festival(Integer categoryID, String name, LocalDateTime startDate, LocalDateTime endDate, Boolean isOngoing, List<DynamicEvent> dynamicEventList) {
        this.CategoryID = categoryID;
        this.Name = name;
        this.StartDate = startDate;
        this.EndDate = endDate;
        this.IsOngoing = isOngoing;
        this.DynamicEventList = dynamicEventList;
    }

    public Festival(Integer categoryID, Boolean isOngoing) {
        this.CategoryID = categoryID;
        this.IsOngoing = isOngoing;
    }

    public Integer getCategoryID() {return CategoryID;}
    public String getName() {return Name;}
    public LocalDateTime getStartDate() {return StartDate;}
    public LocalDateTime getEndDate() {return EndDate;}
    public Boolean getIsOngoing() {return IsOngoing;}
    public List<DynamicEvent> getDynamicEventList() {return DynamicEventList;}
}