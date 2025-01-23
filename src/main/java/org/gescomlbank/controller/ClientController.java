package org.gescomlbank.controller;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.services.clients.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@CrossOrigin("*")
public class ClientController {
    private final ClientService clientService;
    ClientController(
            final ClientService clientService
    ) {
        this.clientService = clientService;
    }

    @PostMapping("/clients")
    void createClient(@RequestBody ClientDto clientDto) {
        this.clientService.createNewClient( clientDto );
    }

    @GetMapping("/clients")
    List<Client> findAll() {
        return this.clientService.findAll();
    }

    @GetMapping("/clients/{id}")
    Client findOne(@PathVariable("id") long id) {
        return this.clientService.findOne(id);
    }
}
