package org.gescomlbank.services.operations;

import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IOperationService {
    void payment(OperationDto operationDto);
    BankAccount withdrawal(OperationDto operationDto);
    boolean transfer(OperationDto operationDto);

    ResponseEntity<Map<String, Object>> findByClientNumAccount(String  numAccount, Pageable pageable);
}
