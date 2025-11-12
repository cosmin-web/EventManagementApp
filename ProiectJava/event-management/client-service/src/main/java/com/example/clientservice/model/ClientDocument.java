package com.example.clientservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "clients")
public class ClientDocument {

    @Id
    private String email;   //campul cheie _id in Mongo
    private String nume;
    private String prenume;
    private Boolean isPublic;
    private Map<String, String> social = new HashMap<>();
    private List<TicketRef> bilete = new ArrayList<>();

    //getters si setters
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

    public List<TicketRef> getBilete() { return bilete; }
    public void setBilete(List<TicketRef> bilete) { this.bilete = bilete; }
}
