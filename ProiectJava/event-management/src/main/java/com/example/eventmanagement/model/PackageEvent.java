package com.example.eventmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pachete_evenimente")
@IdClass(PackageEventIdEntity.class)
public class PackageEvent {

    @Id
    @ManyToOne
    @JoinColumn(name = "pachet_id", nullable = false)
    private PackageEntity pachet;

    @Id
    @ManyToOne
    @JoinColumn(name = "eveniment_id", nullable = false)
    private EventEntity eveniment;

    private Integer numarLocuri;

    public PackageEvent() {}

    public PackageEvent(PackageEntity pachet, EventEntity eveniment, Integer numarLocuri) {
        this.pachet = pachet;
        this.eveniment = eveniment;
        this.numarLocuri = numarLocuri;
    }

    public PackageEntity getPachet() {
        return pachet;
    }
    public void setPachet(PackageEntity pachet) {
        this.pachet = pachet;
    }

    public EventEntity getEveniment() {
        return eveniment;
    }
    public void setEveniment(EventEntity eveniment) {
        this.eveniment = eveniment;
    }

    public Integer getNumarLocuri() {
        return numarLocuri;
    }
    public void setNumarLocuri(Integer numarLocuri) {
        this.numarLocuri = numarLocuri;
    }
}
