package org.gescomlbank.mapper;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.dtos.CurrentAccountDto;
import org.gescomlbank.dtos.SavingAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.Client;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.enums.AccountStatus;
import org.gescomlbank.services.bankaccounts.BankAccountService;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {
    public <T extends BankAccount> T toAccount(BankAccountDto dto, Client client, Class<T> type) {
        try {
            T account = type.getDeclaredConstructor().newInstance();
            account.setClient(client);
            account.setStatus(AccountStatus.ACTIVATED);
            account.setNumAccount(BankAccountService.generateNumAccount());
            account.setBalance(dto.getBalance());
            account.setCurrency(dto.getCurrency());

            if (account instanceof CurrentAccount) {
                ((CurrentAccount) account).setOverdraft(dto.getOverdraft());
            } else if (account instanceof SavingAccount) {
                ((SavingAccount) account).setInterestRate(dto.getInterestRate());
            }
            return account;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du compte", e.getCause());
        }
    }

    public CurrentAccountDto toCurrentDto(BankAccount account) {
        return new CurrentAccountDto(
                ((CurrentAccount) account).getOverdraft(),
                account.getNumAccount(),
                account.getBalance(),
                account.getCurrency(),
                account.getClient().getId(),
                account.getStatus()
        );
    }

    public SavingAccountDto toSavingDto(BankAccount account) {
        return new SavingAccountDto(
                ((SavingAccount) account).getInterestRate(),
                account.getNumAccount(),
                account.getBalance(),
                account.getCurrency(),
                account.getClient().getId(),
                account.getStatus()
        );
    }
}
