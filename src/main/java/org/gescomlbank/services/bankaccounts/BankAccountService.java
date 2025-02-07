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
    BankAccountService(
            final BankAccountRepository bankAccountRepository,
            final ClientRepository clientRepository,
            BankAccountMapper bankAccountMapper,
            ResponseWithPagination responseWithPagination
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.clientRepository = clientRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.responseWithPagination = responseWithPagination;
    }

    @Override
    public ResponseEntity<Map<String, Object>> createBankAccount(BankAccountDto bankAccountDto) {
        Client client = this.clientRepository.findById(bankAccountDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));

        Pageable pageable = PageRequest.of(0, 10);

        if ((bankAccountDto.getOverdraft() > 0 && bankAccountDto.getInterestRate() == 0)) {
            // compte courant
            CurrentAccount currentAccount = bankAccountMapper.toCurrentAccount(bankAccountDto, client);
            this.bankAccountRepository.save(currentAccount);
            Page<CurrentAccount> currentAccountsPaged = bankAccountRepository.findCurrentAccounts(pageable);

            return responseWithPagination.getResponse("Compte courant créer avec succes", currentAccountsPaged);
        }

        if ((bankAccountDto.getOverdraft() == 0 && bankAccountDto.getInterestRate() > 0)) {
            // compte epargne
            SavingAccount savingAccount = bankAccountMapper.toSavingAccount(bankAccountDto, client);
            this.bankAccountRepository.save(savingAccount);
            Page<SavingAccount> savingAccountsPaged = bankAccountRepository.findSavingAccounts(pageable);

            return responseWithPagination.getResponse("Compte epargne créer avec succes", savingAccountsPaged);
        }

        return ResponseUtil.errorsResponse("Les paramètres fournis ne correspondent à aucun type de compte valide", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findSavingAccounts(Pageable pageable) {
        Page<SavingAccount> savingAccountsPaged = bankAccountRepository.findSavingAccounts(pageable);

        return responseWithPagination.getResponse("", savingAccountsPaged);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findCurrentAccounts(Pageable pageable) {
        Page<CurrentAccount> currentAccountsPaged = bankAccountRepository.findCurrentAccounts(pageable);

        return responseWithPagination.getResponse("", currentAccountsPaged);
    }

    @Override
    public ResponseEntity<Map<String, Object>> findOne(String numAccount, String type) {
        return bankAccountRepository.findByNumAccount(numAccount)
                .map(bankAccount -> {
                    if (bankAccount instanceof CurrentAccount && type.equals("CC")) {
                        return ResponseUtil.successResponse("Compte récupéré avec succès",
                                bankAccountMapper.toDto((CurrentAccount) bankAccount));
                    } else if (bankAccount instanceof SavingAccount && type.equals("CS")) {
                        return ResponseUtil.successResponse("Compte récupéré avec succès",
                                bankAccountMapper.toDto((SavingAccount) bankAccount));
                    } else {
                        return ResponseUtil.errorsResponse("Erreur lors de la recuperation compte", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .orElseGet(() -> ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Map<String, Object>> activeAccount(String numAccount) {
           return bankAccountRepository.findByNumAccount(numAccount).map(
                    bankAccount -> {
                        if (bankAccount.getStatus().equals(AccountStatus.SUSPENDED)) {
                            bankAccount.setStatus(AccountStatus.ACTIVATED);
                            this.bankAccountRepository.save(bankAccount);

                            return findAllAccounts(PageRequest.of(0, 10));
                        } else if (bankAccount.getStatus().equals(AccountStatus.ACTIVATED) ){
                            return ResponseUtil.errorsResponse("Cette compte est déjà active", HttpStatus.CONFLICT);
                        } else {
                            return ResponseUtil.errorsResponse("Error lors de l'activation de carte", HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
            ).orElseGet(() -> ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Map<String, Object>> suspendAccount(String numAccount) {
        return bankAccountRepository.findByNumAccount(numAccount).map(
                bankAccount -> {
                    if (bankAccount.getStatus().equals(AccountStatus.ACTIVATED)) {
                        bankAccount.setStatus(AccountStatus.SUSPENDED);
                        this.bankAccountRepository.save(bankAccount);

                        return findAllAccounts(PageRequest.of(0, 10));
                    }else if (bankAccount.getStatus().equals(AccountStatus.SUSPENDED) ){
                        return ResponseUtil.errorsResponse("Cette compte est déjà suspendu", HttpStatus.CONFLICT);
                    }  else {
                        return ResponseUtil.errorsResponse("Error lors de la suspension de compte", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
        ).orElseGet(() -> ResponseUtil.errorsResponse("Compte n'existe pas", HttpStatus.NOT_FOUND));
    }


    public static String generateNumAccount(){
        Random rand = new Random();

        // les 4 premiers chifres sont 0
        StringBuilder sb = new StringBuilder("0000");

        // les 4 suivants sont 0 ou 1
        for (int i = 0; i < 4; i++) {
            sb.append(rand.nextInt(2));
        }

        //les 10 derniers sont generes aleatoirement
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }

        return sb.toString();
    }

    public ResponseEntity<Map<String, Object>> findAllAccounts(Pageable pageable) {
        Page<BankAccount> bankAccounts = bankAccountRepository.findAll(pageable);

        return responseWithPagination.getResponse("", bankAccounts);
    }
}
