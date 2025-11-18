package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class TicketDTO {
    private Integer eventId;
    private Integer packageId;

    @NotBlank(message = "Codul biletului este obligatoriu")
    private String cod;


    public TicketDTO() {}

    public TicketDTO(Integer eventId, Integer packageId, String cod) {
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
