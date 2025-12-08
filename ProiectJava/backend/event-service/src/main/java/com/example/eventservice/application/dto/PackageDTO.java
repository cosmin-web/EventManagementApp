package com.example.eventservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PackageDTO {

    private Integer id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    // @NotNull(message = "Proprietarul este obligatoriu") pt ca ownerid vine acum din token
    private Integer ownerId;

    @NotBlank(message = "Numele pachetului este obligatoriu")
    private String nume;

    @NotBlank(message = "Locatia este obligatorie")
    private String locatie;

    private String descriere;

    @NotNull(message = "Numarul de locuri este obligatoriu")
    @Positive(message = "Numarul de locuri trebuie sa fie pozitiv")
    private Integer numarLocuri;

    //campuri noi
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String ownerEmail;

    private Integer numberOfEvents;
    private Integer availableTickets;

    public PackageDTO() {}

    public PackageDTO(Integer id, Integer ownerId, String nume, String locatie, String descriere, Integer numarLocuri) {
        this.id = id;
        this.ownerId = ownerId;
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
        this.numarLocuri = numarLocuri;

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getLocatie() { return locatie; }
    public void setLocatie(String locatie) { this.locatie = locatie; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public Integer getOwnerId() { return ownerId; }
    public void setOwnerId(Integer ownerId) { this.ownerId = ownerId; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public Integer getNumberOfEvents() { return numberOfEvents; }
    public void setNumberOfEvents(Integer numberOfEvents) { this.numberOfEvents = numberOfEvents; }

    public Integer getAvailableTickets() { return availableTickets; }
    public void setAvailableTickets(Integer availableTickets) { this.availableTickets = availableTickets; }

    public Integer getNumarLocuri() { return numarLocuri; }
    public void setNumarLocuri(Integer numarLocuri) { this.numarLocuri = numarLocuri; }
}
