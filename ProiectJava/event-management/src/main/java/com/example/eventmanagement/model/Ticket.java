package com.example.eventmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bilete")
public class Ticket {

    @Id
    private String cod;

    @ManyToOne
    @JoinColumn(name = "pachet_id")
    private PackageEntity pachet;

    @ManyToOne
    @JoinColumn(name = "eveniment_id")
    private Event eveniment;

    public Ticket() {}

    public Ticket(String cod, PackageEntity pachet, Event eveniment) {
        this.cod = cod;
        this.pachet = pachet;
        this.eveniment = eveniment;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public PackageEntity getPachet() {
        return pachet;
    }

    public void setPachet(PackageEntity pachet) {
        this.pachet = pachet;
    }

    public Event getEveniment() {
        return eveniment;
    }

    public void setEveniment(Event eveniment) {
        this.eveniment = eveniment;
    }
}
