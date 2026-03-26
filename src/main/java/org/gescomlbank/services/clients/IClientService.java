package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IClientService {
    ResponseEntity<Map<String, Object>> createNewClient(ClientDto clientDto);

    ResponseEntity<Map<String, Object>> findAll(Pageable pageable, String search);

    ResponseEntity<Map<String, Object>> findOne(long id);
    
    ResponseEntity<Map<String, Object>> updateClient(long id, ClientDto clientDto);
}
