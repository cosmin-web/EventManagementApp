package com.example.eventmanagement.dto.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientWrapper {

    private PublicClientDTO data;

    public PublicClientDTO getData() { return data; }
    public void setData(PublicClientDTO data) { this.data = data; }
}
