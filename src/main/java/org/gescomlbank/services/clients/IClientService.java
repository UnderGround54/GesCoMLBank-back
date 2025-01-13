package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;

import java.util.List;

public interface IClientService {
    void createNewClient(ClientDto clientDto);

    List<Client> findAll();

    Client findOne(long id);
}
