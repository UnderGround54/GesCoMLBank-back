package org.gescomlbank.services.bankaccounts;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBankAccountService {
    ResponseEntity<Map<String, Object>> createBankAccount(BankAccountDto bankAccountDto);

    ResponseEntity<Map<String, Object>> findSavingAccounts(Pageable pageable);

    ResponseEntity<Map<String, Object>> findCurrentAccounts(Pageable pageable);

    BankAccount findOne(String numAccount);


    boolean activeAccount(String numAccount);

    boolean suspendAccount(String numAccount);
}
