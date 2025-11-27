package com.example.clientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

public class ClientDTO {

    private String id;

    @Email
    @NotBlank
    private String email;

    private String nume;
    private String prenume;
    private Boolean isPublic;
    private Map<String, String> social;
    private List<TicketDTO> bilete;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getPrenume() { return prenume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public Map<String, String> getSocial() { return social; }
    public void setSocial(Map<String, String> social) { this.social = social; }

    public List<TicketDTO> getBilete() { return bilete; }
    public void setBilete(List<TicketDTO> bilete) { this.bilete = bilete; }
}
