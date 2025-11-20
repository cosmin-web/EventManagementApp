package com.example.eventmanagement.integration;

import com.example.eventmanagement.dto.client.ClientWrapper;
import com.example.eventmanagement.dto.client.PublicClientDTO;

import com.example.eventmanagement.dto.client.WrappedClientResponse;
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

        WrappedClientResponse response = clientServiceWebClient.get()
                .uri("/clients/public/by-event/{id}", eventId)
                .retrieve()
                .bodyToMono(WrappedClientResponse.class)
                .block();

        return response.getContent().stream()
                .map(ClientWrapper::getData)
                .toList();
    }

    public List<PublicClientDTO> getClientsByPackage(Integer packageId) {

        WrappedClientResponse response = clientServiceWebClient.get()
                .uri("/clients/public/by-package/{id}", packageId)
                .retrieve()
                .bodyToMono(WrappedClientResponse.class)
                .block();

        return response.getContent().stream()
                .map(ClientWrapper::getData)
                .toList();
    }
}
