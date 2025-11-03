package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class PackageEventDTO {
    @NotNull(message = "ID-ul pachetului este obligatoriu")
    private Integer packageId;

    @NotNull(message = "ID-ul evenimentului este obligatoriu")
    private Integer eventId;

    private Integer numarLocuri;

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
}
