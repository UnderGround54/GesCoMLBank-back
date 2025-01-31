package org.gescomlbank.mapper;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.enums.AccountStatus;
import org.springframework.stereotype.Component;

import static org.gescomlbank.services.bankaccounts.BankAccountService.generateNumAccount;

@Component
public class BankAccountMapper {
    public CurrentAccount toCurrentAccount(BankAccountDto bankAccountDto, Client client) {
        CurrentAccount account = new CurrentAccount();

        account.setClient(client);
        account.setStatus(AccountStatus.ACTIVATED);
        account.setNumAccount(generateNumAccount());
        account.setBalance(bankAccountDto.getBalance());
        account.setCurrency(bankAccountDto.getCurrency());
        account.setOverdraft(bankAccountDto.getOverdraft());

        return account;
    }

    public SavingAccount toSavingAccount(BankAccountDto dto, Client client) {
        SavingAccount account = new SavingAccount();

        account.setClient(client);
        account.setStatus(AccountStatus.ACTIVATED);
        account.setNumAccount(generateNumAccount());
        account.setBalance(dto.getBalance());
        account.setCurrency(dto.getCurrency());
        account.setInterestRate(dto.getInterestRate());

        return account;
    }
}
