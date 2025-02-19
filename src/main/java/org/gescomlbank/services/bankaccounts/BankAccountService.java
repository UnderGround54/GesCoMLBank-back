package org.gescomlbank.services.bankaccounts;

import jakarta.persistence.EntityNotFoundException;
import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.enums.AccountStatus;
import org.gescomlbank.mapper.BankAccountMapper;
import org.gescomlbank.repositories.BankAccountRepository;
import org.gescomlbank.repositories.ClientRepository;
import org.gescomlbank.services.ResponseWithPagination;
import org.gescomlbank.utils.ResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankAccountService implements IBankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final ClientRepository clientRepository;
    private final BankAccountMapper bankAccountMapper;
    private final ResponseWithPagination responseWithPagination;

    public BankAccountService(BankAccountRepository bankAccountRepository,
                              ClientRepository clientRepository,
                              BankAccountMapper bankAccountMapper,
                              ResponseWithPagination responseWithPagination) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.responseWithPagination = responseWithPagination;
    }

    @Override
    public ResponseEntity<Map<String, Object>> createBankAccount(BankAccountDto bankAccountDto) {
        Client client = clientRepository.findById(bankAccountDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));

        Pageable pageable = PageRequest.of(0, 10);

        if (bankAccountDto.getOverdraft() > 0 && bankAccountDto.getInterestRate() == 0) {
            return saveAndResponse(bankAccountDto, client, CurrentAccount.class, "Compte courant créé avec succès", pageable);
        } else if (bankAccountDto.getOverdraft() == 0 && bankAccountDto.getInterestRate() > 0) {
            return saveAndResponse(bankAccountDto, client, SavingAccount.class, "Compte épargne créé avec succès", pageable);
        }
        return ResponseUtil.errorsResponse("Les paramètres fournis ne correspondent à aucun type de compte valide", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findSavingAccounts(Pageable pageable) {
        Page<SavingAccount> savingAccounts = bankAccountRepository.findSavingAccounts(pageable);
        return responseWithPagination.getResponse("", savingAccounts);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findCurrentAccounts(Pageable pageable) {
        Page<CurrentAccount> currentAccountsPaged = bankAccountRepository.findCurrentAccounts(pageable);
        return responseWithPagination.getResponse("", currentAccountsPaged);
    }

    private <T extends BankAccount> ResponseEntity<Map<String, Object>> saveAndResponse(BankAccountDto dto, Client client, Class<T> type, String message, Pageable pageable) {
        T account = bankAccountMapper.toAccount(dto, client, type);
        bankAccountRepository.save(account);
        Page<BankAccount> accountsPaged =  bankAccountRepository.findAll(pageable);

        return responseWithPagination.getResponse(message, accountsPaged);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findOne(String numAccount, String type) {
        return bankAccountRepository.findByNumAccount(numAccount)
                .map(account -> {
                    if (account instanceof CurrentAccount && "CC".equals(type)) {
                        return ResponseUtil.successResponse("Compte récupéré avec succès", bankAccountMapper.toCurrentDto(account));
                    } else if (account instanceof SavingAccount && "CS".equals(type)) {
                        return ResponseUtil.successResponse("Compte récupéré avec succès", bankAccountMapper.toSavingDto(account));
                    }
                    return  ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND);
                })
                .orElseGet(() -> ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<Map<String, Object>> changeAccountStatus(String numAccount, AccountStatus newStatus, String errorMessage) {
        return bankAccountRepository.findByNumAccount(numAccount)
                .map(account -> {
                    if (!account.getStatus().equals(newStatus)) {
                        account.setStatus(newStatus);
                        bankAccountRepository.save(account);
                        return findAllAccounts(PageRequest.of(0, 10));
                    }
                    return ResponseUtil.errorsResponse(errorMessage, HttpStatus.CONFLICT);
                })
                .orElseGet(() -> ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Map<String, Object>> activeAccount(String numAccount) {
        return changeAccountStatus(numAccount, AccountStatus.ACTIVATED, "Ce compte est déjà actif");
    }

    @Override
    public ResponseEntity<Map<String, Object>> suspendAccount(String numAccount) {
        return changeAccountStatus(numAccount, AccountStatus.SUSPENDED, "Ce compte est déjà suspendu");
    }

    public static String generateNumAccount() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder("0000");
        for (int i = 0; i < 4; i++) sb.append(rand.nextInt(2));
        for (int i = 0; i < 10; i++) sb.append(rand.nextInt(10));
        return sb.toString();
    }

    public ResponseEntity<Map<String, Object>> findAllAccounts(Pageable pageable) {
        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(pageable);
        return responseWithPagination.getResponse("", bankAccounts);
    }
}
