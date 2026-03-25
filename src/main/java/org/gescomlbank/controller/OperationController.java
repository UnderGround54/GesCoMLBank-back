package org.gescomlbank.controller;

import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.Operation;
import org.gescomlbank.services.operations.OperationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class OperationController {
    private final OperationService operationService;
    OperationController( OperationService operationService ) {
        this.operationService = operationService;
    }

    @PostMapping("/operations/payment")
    void payment(@RequestBody OperationDto operationDto ) {
        this.operationService.payment(operationDto);
    }

    @PostMapping("/operations/withdrawal")
    void withdrawal(@RequestBody OperationDto operationDto) {
        this.operationService.withdrawal(operationDto);
    }

    @PostMapping("/operations/transfer")
    boolean transfer(@RequestBody OperationDto operationDto) {
        return this.operationService.transfer( operationDto );
    }

    @GetMapping("/operations/client/{numAccount}")
    ResponseEntity<Map<String, Object>> getClientOperations(
            @PathVariable("numAccount") String numAccount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return this.operationService.findByClientNumAccount(numAccount, pageable);
    }
}
