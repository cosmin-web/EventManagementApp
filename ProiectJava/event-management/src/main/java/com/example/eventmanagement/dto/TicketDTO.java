package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class TicketDTO {
    @NotBlank(message = "Codul biletului este obligatoriu")
    private String cod;

    private Integer packageId;
    private Integer eventId;

    public TicketDTO() {}

    public TicketDTO(String cod, Integer packageId, Integer eventId) {
        this.cod = cod;
        this.packageId = packageId;
        this.eventId = eventId;
    }

    public String getCod() { return cod; }
    public void setCod(String cod) { this.cod = cod; }

    public Integer getPackageId() { return packageId; }
    public void setPackageId(Integer packageId) { this.packageId = packageId; }

    public Integer getEventId() { return eventId; }
    public void setEventId(Integer eventId) { this.eventId = eventId; }
}
