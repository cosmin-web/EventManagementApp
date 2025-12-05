package com.example.clientservice.infrastructure.adapter.out.event;

import com.example.clientservice.infrastructure.adapter.out.event.dto.TicketData;
import com.example.clientservice.infrastructure.adapter.out.event.dto.WrappedTicketResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class EventApiClient {
    private final WebClient eventWebClient;

    public EventApiClient(WebClient eventWebClient) {
        this.eventWebClient = eventWebClient;
    }

    public TicketData validateAndFetchTicket(String cod, String authorizationHeader) {
        return eventWebClient.get()
                .uri(uri -> uri.path("/tickets/{cod}").build(cod))
                .header("Authorization", authorizationHeader)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("Biletul nu a fost gasit: " + cod)))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Server eroare")))
                .bodyToMono(WrappedTicketResponse.class)
                .map(WrappedTicketResponse::getData)
                .onErrorResume(e -> {
                    return Mono.empty();
                })
                .block();
    }


}
