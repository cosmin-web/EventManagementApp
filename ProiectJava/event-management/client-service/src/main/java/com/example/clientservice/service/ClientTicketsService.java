package com.example.clientservice.service;

import com.example.clientservice.integration.EventApiClient;
import com.example.clientservice.integration.TicketData;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.model.TicketRef;
import com.example.clientservice.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientTicketsService {

    private final ClientRepository repo;
    private final EventApiClient eventApiClient;

    public ClientTicketsService(ClientRepository repo, EventApiClient eventApiClient) {
        this.repo = repo;
        this.eventApiClient = eventApiClient;
    }

    public Optional<TicketData> validateTicket(String email, String cod, boolean saveIfValid) {
        var data = eventApiClient.validateAndFetchTicket(cod);
        if (data == null) {
            return Optional.empty();
        }

        if (saveIfValid) {
            ClientDocument client = repo.findByEmail(email).orElseGet(() -> {
                ClientDocument c = new ClientDocument();
                c.setEmail(email);
                return c;
            });

            boolean exists = client.getBilete().stream()
                    .anyMatch(t -> cod.equals(t.getCod()));

            if (!exists) {
                TicketRef ref = new TicketRef();
                ref.setCod(cod);
                if (data.getEvent() != null) {
                    ref.setTip("event");
                    ref.setEventId(data.getEvent().getId());
                } else if (data.get_package() != null) {
                    ref.setTip("package");
                    ref.setPackageId(data.get_package().getId());
                }
                client.getBilete().add(ref);
                repo.save(client);
            }
        }

        return Optional.of(data);
    }

    public List<TicketData> listDetailedTickets(String email) {
        return repo.findByEmail(email)
                .map(ClientDocument::getBilete)
                .orElseGet(List::of)
                .stream()
                .map(TicketRef::getCod)
                .map(eventApiClient::validateAndFetchTicket)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
