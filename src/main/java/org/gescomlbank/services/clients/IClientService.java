package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IClientService {
    ResponseEntity<Map<String, Object>> createNewClient(ClientDto clientDto);

    ResponseEntity<Map<String, Object>> findAll();

    ResponseEntity<Map<String, Object>> findOne(long id);
}
