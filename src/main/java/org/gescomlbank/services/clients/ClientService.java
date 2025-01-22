package org.gescomlbank.services.clients;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements IClientService {

    private final ClientRepository clientRepository;
    ClientService(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createNewClient(ClientDto clientDto) {
        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setMail(clientDto.getEmail());
        client.setAddress(clientDto.getAddress());
        client.setTelephone(clientDto.getTelephone());
        client.setBirthDate(clientDto.getBirthDate());

        this.clientRepository.save(client);
    }

    @Override
    public List<Client> findAll() {
        return this.clientRepository.findAll();
    }

    @Override
    public Client findOne(long id) {
        return this.clientRepository.getReferenceById(id);
    }
}
