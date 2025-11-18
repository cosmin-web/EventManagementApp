package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class PackageEventDTO {
    @NotNull(message = "ID-ul pachetului este obligatoriu")
    private Integer packageId;

    @NotNull(message = "ID-ul evenimentului este obligatoriu")
    private Integer eventId;

    //campuri noi
    private String packageName;
    private String eventName;
    private String eventLocation;

    public PackageEventDTO() {}

    public PackageEventDTO(Integer packageId, Integer eventId) {
        this.packageId = packageId;
        this.eventId = eventId;
    }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }
}
