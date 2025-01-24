package org.gescomlbank.controller;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.services.clients.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> createClient(@RequestBody ClientDto clientDto) {
        return this.clientService.createNewClient( clientDto );
    }

    @GetMapping("/clients")
    ResponseEntity<Map<String, Object>> findAll() {
        return this.clientService.findAll();
    }

    @GetMapping("/clients/{id}")
    Client findOne(@PathVariable("id") long id) {
        return this.clientService.findOne(id);
    }
}
