package com.example.clientservice.infrastructure.adapter.out.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrappedTicketResponse {
    private TicketData data;

    public WrappedTicketResponse() {}

    public WrappedTicketResponse(TicketData data) {
        this.data = data;
    }

    public TicketData getData() { return data; }
    public void setData(TicketData data) { this.data = data; }
}
