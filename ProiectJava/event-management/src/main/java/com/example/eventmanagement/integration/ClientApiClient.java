package com.example.eventmanagement.integration;

import com.example.eventmanagement.dto.client.PublicClientDTO;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ClientApiClient {

    private final WebClient clientServiceWebClient;

    public ClientApiClient(WebClient clientServiceWebClient) {
        this.clientServiceWebClient = clientServiceWebClient;
    }

    public List<PublicClientDTO> getClientsByEvent(Integer eventId) {
        return clientServiceWebClient.get()
                .uri("/clients/public/by-event/{id}", eventId)
                .retrieve()
                .bodyToFlux(PublicClientDTO.class)
                .collectList()
                .block();
    }

    public List<PublicClientDTO> getClientsByPackage(Integer packageId) {
        return clientServiceWebClient.get()
                .uri("/clients/public/by-package/{id}", packageId)
                .retrieve()
                .bodyToFlux(PublicClientDTO.class)
                .collectList()
                .block();
    }
}
