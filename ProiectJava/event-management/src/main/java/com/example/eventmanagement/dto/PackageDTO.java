package com.example.eventmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PackageDTO {

    private Integer id;

    @NotNull(message = "Proprietarul este obligatoriu")
    private Integer ownerId;

    @NotBlank(message = "Numele pachetului este obligatoriu")
    private String nume;

    @NotBlank(message = "Locatia este obligatorie")
    private String locatie;

    private String descriere;

    public PackageDTO() {}

    public PackageDTO(Integer id, Integer ownerId, String nume, String locatie, String descriere) {
        this.id = id;
        this.ownerId = ownerId;
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
    }

    // getters & setters
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
}
