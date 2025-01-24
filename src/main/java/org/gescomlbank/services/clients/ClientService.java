package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.repositories.ClientRepository;
import org.gescomlbank.services.utils.ResponseUtil;
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
    ClientService(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> createNewClient(ClientDto clientDto) {
        try {
            Client client = buildClientFromDto(clientDto);
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

    /**
     *
     */
    private Client buildClientFromDto(ClientDto clientDto) {
        Client client = new Client();

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setMail(clientDto.getEmail());
        client.setAddress(clientDto.getAddress());
        client.setTelephone(clientDto.getTelephone());
        client.setBirthDate(clientDto.getBirthDate());

        return client;
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
    public Client findOne(long id) {
        return this.clientRepository.getReferenceById(id);
    }
}
