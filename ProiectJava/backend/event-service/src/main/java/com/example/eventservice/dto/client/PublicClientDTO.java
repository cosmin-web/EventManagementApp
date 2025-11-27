package com.example.eventservice.dto.client;

import com.example.eventservice.dto.TicketDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicClientDTO {

    private String id;
    private String email;
    private String nume;
    private String prenume;
    private boolean isPublic;
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

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean aPublic) { isPublic = aPublic; }

    public Map<String, String> getSocial() { return social; }
    public void setSocial(Map<String, String> social) { this.social = social; }

    public List<TicketDTO> getBilete() { return bilete; }
    public void setBilete(List<TicketDTO> bilete) { this.bilete = bilete; }
}
