package com.example.eventmanagement.model;

import java.io.Serializable;
import java.util.Objects;

public class PackageEventId implements Serializable {

    private Integer pachet;
    private Integer eveniment;

    public PackageEventId() {}

    public PackageEventId(Integer pachet, Integer eveniment) {
        this.pachet = pachet;
        this.eveniment = eveniment;
    }

    public Integer getPachet() {
        return pachet;
    }

    public void setPachet(Integer pachet) {
        this.pachet = pachet;
    }

    public Integer getEveniment() {
        return eveniment;
    }

    public void setEveniment(Integer eveniment) {
        this.eveniment = eveniment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PackageEventId)) return false;
        PackageEventId that = (PackageEventId) o;
        return Objects.equals(pachet, that.pachet) && Objects.equals(eveniment, that.eveniment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pachet, eveniment);
    }
}
