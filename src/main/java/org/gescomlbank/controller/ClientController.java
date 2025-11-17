package org.gescomlbank.controller;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.services.clients.ClientService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/api")
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
    ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.clientService.findAll(pageable);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Map<String, Object>> findOne(@PathVariable("id") long id) {
        return this.clientService.findOne(id);
    }
}
