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
        client.setMail(clientDto.getMail());
        client.setAddress(clientDto.getAddress());
        client.setTelephone(clientDto.getTelephone());
        client.setBirthDate(clientDto.getBirthDate());

        return client;
    }

    public void updateEntity(Client client, ClientDto clientDto) {
        if (clientDto.getFirstName() != null) client.setFirstName(clientDto.getFirstName());
        if (clientDto.getLastName()  != null) client.setLastName(clientDto.getLastName());
        if (clientDto.getBirthDate() != null) client.setBirthDate(clientDto.getBirthDate());
        if (clientDto.getTelephone() != null) client.setTelephone(clientDto.getTelephone());
        if (clientDto.getMail()      != null) client.setMail(clientDto.getMail());
        if (clientDto.getAddress()   != null) client.setAddress(clientDto.getAddress());
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
