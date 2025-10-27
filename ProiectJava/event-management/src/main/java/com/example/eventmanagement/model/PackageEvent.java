package com.example.eventmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pachete_evenimente")
@IdClass(PackageEventId.class)
public class PackageEvent {

    @Id
    @ManyToOne
    @JoinColumn(name = "pachet_id", nullable = false)
    private Package pachet;

    @Id
    @ManyToOne
    @JoinColumn(name = "eveniment_id", nullable = false)
    private Event eveniment;

    private Integer numarLocuri;

    public PackageEvent() {}

    public PackageEvent(Package pachet, Event eveniment, Integer numarLocuri) {
        this.pachet = pachet;
        this.eveniment = eveniment;
        this.numarLocuri = numarLocuri;
    }

    public Package getPachet() {
        return pachet;
    }

    public void setPachet(Package pachet) {
        this.pachet = pachet;
    }

    public Event getEveniment() {
        return eveniment;
    }

    public void setEveniment(Event eveniment) {
        this.eveniment = eveniment;
    }

    public Integer getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(Integer numarLocuri) {
        this.numarLocuri = numarLocuri;
    }
}
