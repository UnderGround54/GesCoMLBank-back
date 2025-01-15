package org.gescomlbank.services.operations;

import lombok.extern.slf4j.Slf4j;
import org.gescomlbank.dtos.OperationDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Operation;
import org.gescomlbank.enums.AccountStatus;
import org.gescomlbank.enums.OperationType;
import org.gescomlbank.repositories.BankAccountRepository;
import org.gescomlbank.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class OperationService implements IOperationService{

    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    OperationService(
            final BankAccountRepository bankAccountRepository,
            final OperationRepository operationRepository
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.operationRepository = operationRepository;
    }

    @Override
    public void payment(OperationDto operationDto) {
        System.out.println(operationDto.getNumAccountSource());
        Optional<BankAccount> bankAccount = this.bankAccountRepository.findByNumAccount(operationDto.getNumAccountSource());
        if (bankAccount.isPresent()) {
            BankAccount account = bankAccount.get();
            if (account.getStatus().equals(AccountStatus.ACTIVATED)) {
                account.setBalance(account.getBalance() + operationDto.getAmount());
                Operation operation = new Operation();
                operation.setDateOperation(new Date());
                operation.setAmount(operationDto.getAmount());
                operation.setOperationType(OperationType.CREDIT);
                operation.setBankAccount(account);
                operation.setNumOperation(generateNumOperation());
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
                Operation operation = new Operation();
                operation.setDateOperation(new Date());
                operation.setAmount(operationDto.getAmount());
                operation.setOperationType(OperationType.DEBIT);
                operation.setBankAccount(account);
                operation.setNumOperation(generateNumOperation());
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
                accountDestination,
                operationDto.getAmount()
        );
        BankAccount bankAccountSource = this.withdrawal(operationDtoSource);

        if (bankAccountSource != null) {
            String numAccountDestination = operationDto.getNumAccountDestination();
            OperationDto operationDtoDestination = new OperationDto(accountSource, numAccountDestination, operationDto.getAmount());
            this.payment(operationDtoDestination);

            return true;
        }
        return false;
    }

    @Override
    public List<Operation> findByClientNumAccount(String numAccount) {
        List<Operation> list = new ArrayList<>();
        for (Operation operation : this.operationRepository.findAll()) {
            if (operation.getBankAccount().getNumAccount().equals(numAccount)) {
                list.add(operation);
            }
        }
        return list;
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
