package com.example.clientservice.mapper;

import com.example.clientservice.dto.ClientDTO;
import com.example.clientservice.dto.TicketDTO;
import com.example.clientservice.model.ClientDocument;
import com.example.clientservice.model.TicketRef;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMapper {

    public static ClientDTO toDTO(ClientDocument d) {
        ClientDTO dto = new ClientDTO();
        dto.setEmail(d.getEmail());
        dto.setNume(d.getNume());
        dto.setPrenume(d.getPrenume());
        dto.setIsPublic(d.getIsPublic());
        dto.setSocial(d.getSocial());

        List<TicketRef> bilete = d.getBilete() != null ? d.getBilete() : Collections.emptyList();
        dto.setBilete(bilete.stream()
                .map(ClientMapper::toTicketDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    public static ClientDocument toDocument(ClientDTO dto) {
        ClientDocument d = new ClientDocument();
        d.setEmail(dto.getEmail());
        d.setNume(dto.getNume());
        d.setPrenume(dto.getPrenume());
        d.setIsPublic(dto.getIsPublic());
        d.setSocial(dto.getSocial());

        List<TicketDTO> bilete = dto.getBilete() != null ? dto.getBilete() : Collections.emptyList();
        d.setBilete(bilete.stream()
                .map(ClientMapper::toTicketRef)
                .collect(Collectors.toList()));

        return d;
    }

    private static TicketDTO toTicketDTO(TicketRef t) {
        TicketDTO dto = new TicketDTO();
        dto.setCod(t.getCod());
        dto.setTip(t.getTip());
        dto.setEventId(t.getEventId());
        dto.setPackageId(t.getPackageId());
        return dto;
    }

    private static TicketRef toTicketRef(TicketDTO dto) {
        TicketRef ref = new TicketRef();
        ref.setCod(dto.getCod());
        ref.setTip(dto.getTip());
        ref.setEventId(dto.getEventId());
        ref.setPackageId(dto.getPackageId());
        return ref;
    }
}
