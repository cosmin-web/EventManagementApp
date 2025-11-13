package com.example.clientservice.integration;

public class PackageShort {
    private Integer id;
    private String nume;
    private String locatie;
    private String descriere;

    public PackageShort() {
    }

    public PackageShort(Integer id, String nume, String locatie, String descriere) {
        this.id = id;
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
    }

    public Integer getId() { return id; }
    public String getNume() { return nume; }
    public String getLocatie() { return locatie; }
    public String getDescriere() { return descriere; }

    public void setId(Integer id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setLocatie(String locatie) { this.locatie = locatie; }
    public void setDescriere(String descriere) { this.descriere = descriere; }
}
