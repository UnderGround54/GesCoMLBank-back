package org.gescomlbank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gescomlbank.enums.AccountStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccountDto {
    private double overdraft;

    private String numAccount;

    private double balance;

    private String currency;

    private long clientId;

    private AccountStatus status;
}
