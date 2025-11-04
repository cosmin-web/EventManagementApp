package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class PackageEventDTO {
    @NotNull(message = "ID-ul pachetului este obligatoriu")
    private Integer packageId;

    @NotNull(message = "ID-ul evenimentului este obligatoriu")
    private Integer eventId;

    private Integer numarLocuri;

    //campuri noi
    private String packageName;
    private String eventName;
    private String eventLocation;

    public PackageEventDTO() {}

    public PackageEventDTO(Integer packageId, Integer eventId, Integer numarLocuri) {
        this.packageId = packageId;
        this.eventId = eventId;
        this.numarLocuri = numarLocuri;
    }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }

    public Integer getNumarLocuri() { return numarLocuri; }
    public void setNumarLocuri(Integer numarLocuri) { this.numarLocuri = numarLocuri; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }
}
