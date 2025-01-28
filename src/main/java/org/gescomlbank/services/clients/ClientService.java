package org.gescomlbank.services.clients;

import jakarta.persistence.EntityNotFoundException;
import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.mapper.ClientMapper;
import org.gescomlbank.repositories.ClientRepository;
import org.gescomlbank.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    ClientService(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = new ClientMapper();
    }

    @Override
    public ResponseEntity<Map<String, Object>> createNewClient(ClientDto clientDto) {
        try {
            Client client = this.clientMapper.toEntity(clientDto);
            client.setCreatedAt(Instant.now());

            this.clientRepository.save(client);
            List<Client> clients = this.clientRepository.findAll();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("list", clients);

           return ResponseUtil.successResponse("Création avec succés", responseData);
        } catch (Exception e){
            return ResponseUtil.errorsResponse(
                    "Erreur lors de la création : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<Client> clients = this.clientRepository.findAll();
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("list", clients);

            return ResponseUtil.successResponse("Clients récupérés avec succès", responseData);
        } catch (Exception e) {
            return ResponseUtil.errorsResponse(
                    "Erreur lors de la récupération des clients : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> findOne(long id) {
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Client n'existe pas"));

            ClientDto clientDto = clientMapper.toDto(client);

            return ResponseUtil.successResponse("Client récupéré avec succès", clientDto);
        } catch (EntityNotFoundException e) {
            return ResponseUtil.errorsResponse(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return ResponseUtil.errorsResponse(
                    "Erreur lors de la récupération du client : " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
