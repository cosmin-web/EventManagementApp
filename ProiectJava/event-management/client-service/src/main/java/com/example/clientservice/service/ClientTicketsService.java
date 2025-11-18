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
        TicketData data = eventApiClient.validateAndFetchTicket(cod);

        if (data == null) {
            throw new RuntimeException("Biletul nu a fost gasit.");
        }

        TicketRef ref = new TicketRef();
        ref.setCod(cod);

        if (data.getEvent() != null) {
            ref.setTip("event");
            ref.setEventId(data.getEvent().getId());
        } else if (data.get_package() != null) {
            ref.setTip("package");
            ref.setPackageId(data.get_package().getId());
        } else {
            throw new RuntimeException("Bilet invalid.");
        }

        if (saveIfValid) {
            ClientDocument client = repo.findByEmail(email).orElse(null);

            if (client == null) {
                client = new ClientDocument();
                client.setEmail(email);
                client.setBilete(new ArrayList<>());
            }

            client.getBilete().add(ref);
            repo.save(client);
        }

        return Optional.of(data);
    }

    public List<TicketData> listDetailedTickets(String email) {
        ClientDocument client = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Clientul nu a fost gasit."));

        List<TicketData> result = new ArrayList<>();

        for (TicketRef ref : client.getBilete()) {
            TicketData data = eventApiClient.validateAndFetchTicket(ref.getCod());
            if (data != null) {
                result.add(data);
            }
        }

        return result;
    }
}
