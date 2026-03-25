package org.gescomlbank.services.operations;

import lombok.extern.slf4j.Slf4j;
import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.Operation;
import org.gescomlbank.enums.AccountStatus;
import org.gescomlbank.enums.OperationType;
import org.gescomlbank.mapper.OperationMapper;
import org.gescomlbank.repositories.BankAccountRepository;
import org.gescomlbank.repositories.OperationRepository;
import org.gescomlbank.services.ResponseWithPagination;
import org.gescomlbank.services.bankaccounts.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OperationService implements IOperationService{

    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final ResponseWithPagination responseWithPagination;
    OperationService(
            final BankAccountRepository bankAccountRepository,
            final OperationRepository operationRepository,
            final OperationMapper operationMapper,
            ResponseWithPagination responseWithPagination
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.operationRepository = operationRepository;
        this.operationMapper = operationMapper;
        this.responseWithPagination = responseWithPagination;
    }

    @Override
    public void payment(OperationDto operationDto) {
        Optional<BankAccount> bankAccount = this.bankAccountRepository.findByNumAccount(operationDto.getNumAccountDestination());
        if (bankAccount.isPresent()) {
            BankAccount account = bankAccount.get();
            if (account.getStatus().equals(AccountStatus.ACTIVATED)) {
                account.setBalance(account.getBalance() + operationDto.getAmount());
                Operation operation = this.operationMapper.toEntity(operationDto, account, OperationType.CREDIT);
                this.operationRepository.save(operation);

                this.bankAccountRepository.save(account);
            } else {
                throw new RuntimeException("Opération impossible, raison : le compte est suspendu");
            }
        } else {
            throw new RuntimeException("Ce compte n'existe pas");
        }
    }

    @Override
    public BankAccount withdrawal(OperationDto operationDto) {
        Optional<BankAccount> bankAccount = this.bankAccountRepository.findByNumAccount(operationDto.getNumAccountSource());
        if (bankAccount.isPresent()) {
            BankAccount account = bankAccount.get();
            if (account.getStatus().equals(AccountStatus.ACTIVATED) && account.getBalance() > operationDto.getAmount()) {
                account.setBalance(account.getBalance() - operationDto.getAmount());
                Operation operation = this.operationMapper.toEntity(operationDto, account, OperationType.DEBIT);
                this.operationRepository.save(operation);

                return this.bankAccountRepository.save(account);
            } else {
                throw new RuntimeException("Solde insuffisant");
            }
        } else {
            throw new RuntimeException("Ce compte n'existe pas");
        }
    }

    @Override
    public boolean transfer(OperationDto operationDto) {
        String accountSource = operationDto.getNumAccountSource();
        String accountDestination = operationDto.getNumAccountDestination();
        OperationDto operationDtoSource = new OperationDto(
                accountSource,
                null,
                operationDto.getAmount()
        );
        BankAccount bankAccountSource = this.withdrawal(operationDtoSource);

        if (bankAccountSource != null) {
            String numAccountDestination = operationDto.getNumAccountDestination();
            OperationDto operationDtoDestination = new OperationDto(null, numAccountDestination, operationDto.getAmount());
            this.payment(operationDtoDestination);

            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<Map<String, Object>> findByClientNumAccount(String numAccount, Pageable pageable) {
        Page<Operation> operationPaged = this.operationRepository.findAll(pageable);

        return this.responseWithPagination.getResponse(
                "",
                operationPaged
        );
    }
}
