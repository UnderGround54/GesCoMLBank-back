package org.gescomlbank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gescomlbank.enums.AccountStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavingAccountDto {
    private double interestRate;

    private String numAccount;

    private double balance;

    private String currency;

    private long clientId;

    private AccountStatus status;
}
