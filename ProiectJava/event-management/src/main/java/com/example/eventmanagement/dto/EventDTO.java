package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EventDTO {

    private Integer id;

    @NotBlank(message = "Numele evenimentului este obligatoriu.")
    private String nume;

    private String locatie;
    private String descriere;

    @NotNull(message = "Numarul de locuri este obligatoriu.")
    @Positive(message = "Numarul de locuri trebuie sa fie pozitiv.")
    private Integer numarLocuri;

    @NotNull(message = "Id-ul proprietarului este obligatoriu.")
    private Integer ownerId;

    public EventDTO() {}

    public EventDTO(Integer id, String nume, String locatie, String descriere, Integer numarLocuri, Integer ownerId) {
        this.id = id;
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
        this.numarLocuri = numarLocuri;
        this.ownerId = ownerId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getLocatie() { return locatie; }
    public void setLocatie(String locatie) { this.locatie = locatie; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public Integer getNumarLocuri() { return numarLocuri; }
    public void setNumarLocuri(Integer numarLocuri) { this.numarLocuri = numarLocuri; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }
}
