package com.example.eventmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pachete")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_owner", nullable = false)
    private User owner;

    @Column(nullable = false, unique = true)
    private String nume;

    private String locatie;
    private String descriere;

    public Package() {}

    public Package(Integer id, User owner, String nume, String locatie, String descriere) {
        this.id = id;
        this.owner = owner;
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
