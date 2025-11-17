package org.gescomlbank.mapper;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public Client toEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setMail(clientDto.getEmail());
        client.setAddress(clientDto.getAddress());
        client.setTelephone(clientDto.getTelephone());
        client.setBirthDate(clientDto.getBirthDate());

        return client;
    }

    public ClientDto toDto(Client client) {
        return new ClientDto(
                client.getFirstName(),
                client.getLastName(),
                client.getBirthDate(),
                client.getTelephone(),
                client.getMail(),
                client.getAddress()
        );
    }
}
