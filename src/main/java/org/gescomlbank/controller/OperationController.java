package org.gescomlbank.controller;

import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.Operation;
import org.gescomlbank.services.operations.OperationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    void withdrawal(@RequestBody OperationDto operationDto ) {
        this.operationService.withdrawal(operationDto);
    }

    @PostMapping("/operations/transfer")
    boolean transfer(@RequestBody OperationDto operationDto) {
        return this.operationService.transfer( operationDto );
    }

    @GetMapping("/operations/client/{numAccount}")
    List<Operation> getClientOperations(@PathVariable("numAccount") String numAccount) {
        return this.operationService.findByClientNumAccount(numAccount);
    }
}
