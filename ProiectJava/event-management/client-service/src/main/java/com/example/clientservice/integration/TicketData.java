package com.example.clientservice.integration;

public class TicketData {
    private String cod;
    private EventShort event;
    private PackageShort _package;

    public TicketData() {
    }

    public TicketData(String cod, EventShort event, PackageShort _package) {
        this.cod = cod;
        this.event = event;
        this._package = _package;
    }

    public String getCod() { return cod; }
    public EventShort getEvent() { return event; }
    public PackageShort get_package() { return _package; }

    public void setCod(String cod) { this.cod = cod; }
    public void setEvent(EventShort event) { this.event = event; }
    public void set_package(PackageShort _package) { this._package = _package; }
}
