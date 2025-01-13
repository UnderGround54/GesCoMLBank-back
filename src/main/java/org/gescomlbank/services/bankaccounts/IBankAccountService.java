package org.gescomlbank.services.bankaccounts;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;

import java.util.List;
import java.util.Optional;

public interface IBankAccountService {
    void createBankAccount(BankAccountDto bankAccountDto);

    List<SavingAccount> findSavingAccounts();

    List<CurrentAccount> findCurrentAccounts();

    BankAccount findOne(String numAccount);


    boolean activeAccount(String numAccount);

    boolean suspendAccount(String numAccount);
}
