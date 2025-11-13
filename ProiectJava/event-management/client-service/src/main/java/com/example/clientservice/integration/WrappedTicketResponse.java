package com.example.clientservice.integration;

public class WrappedTicketResponse {
    private TicketData data;

    public WrappedTicketResponse() {
    }

    public WrappedTicketResponse(TicketData data) {
        this.data = data;
    }

    public TicketData getData() { return data; }
    public void setData(TicketData data) { this.data = data; }
}
