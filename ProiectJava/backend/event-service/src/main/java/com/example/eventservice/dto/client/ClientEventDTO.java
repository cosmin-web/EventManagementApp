package com.example.eventservice.dto.client;

public class ClientEventDTO {

    private String clientId;
    private Integer eventId;

    public ClientEventDTO() {}

    public ClientEventDTO(String clientId, Integer eventId) {
        this.clientId = clientId;
        this.eventId = eventId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
