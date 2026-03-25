package org.gescomlbank.mapper;

import org.gescomlbank.dtos.ClientDto;
import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.Operation;
import org.gescomlbank.enums.OperationType;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

@Component
public class OperationMapper {
    public Operation toEntity(OperationDto operationDto, BankAccount account, OperationType type) {
        Operation operation = new Operation();
        operation.setDateOperation(new Date());
        operation.setAmount(operationDto.getAmount());
        operation.setOperationType(type);
        operation.setBankAccount(account);
        operation.setNumOperation(generateNumOperation());

        return operation;
    }

    public ClientDto toDto(Client client) {
        return new ClientDto(
                client.getFirstName(),
                client.getLastName(),
                client.getBirthDate(),
                client.getTelephone(),
                client.getMail(),
                client.getAddress()
        );
    }

    private static String generateNumOperation() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("00");

        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(2));
        }
        for (int i = 0; i < 3; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
