package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.mapper.ClientMapper;
import org.gescomlbank.repositories.ClientRepository;
import org.gescomlbank.services.ResponseWithPagination;
import org.gescomlbank.utils.ResponseUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ResponseWithPagination responseWithPagination;
    ClientService(
            final ClientRepository clientRepository,
            final ClientMapper clientMapper,
            ResponseWithPagination responseWithPagination
    ) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.responseWithPagination = responseWithPagination;
    }

    @Override
    public ResponseEntity<Map<String, Object>> createNewClient(ClientDto clientDto) {
        Client client = this.clientMapper.toEntity(clientDto);
        this.clientRepository.save(client);
        Pageable pageable = PageRequest.of(0, 10);

        return this.responseWithPagination.getResponse("Création avec succés", pageable, this.clientRepository);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findAll(Pageable pageable) {
        return this.responseWithPagination.getResponse(
                "",
                pageable,
                this.clientRepository
        );
    }

    @Override
    public ResponseEntity<Map<String, Object>> findOne(long id) {
        return clientRepository.findById(id)
                .map(client -> ResponseUtil.successResponse("Client récupéré avec succès", clientMapper.toDto(client)))
                .orElseGet(() -> ResponseUtil.errorsResponse("Client n'existe pas", HttpStatus.NOT_FOUND));
    }
}
