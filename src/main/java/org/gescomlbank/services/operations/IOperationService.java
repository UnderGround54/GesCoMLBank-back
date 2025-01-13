package org.gescomlbank.services.operations;

import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Operation;

import java.util.List;

public interface IOperationService {
    void payment(OperationDto operationDto);
    BankAccount withdrawal(OperationDto operationDto);
    boolean transfer(OperationDto operationDto);

    List<Operation> findByClientNumAccount(String  numAccount);
}
