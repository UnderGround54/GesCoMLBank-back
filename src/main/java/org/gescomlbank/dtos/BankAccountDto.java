package org.gescomlbank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDto {
    private double balance;

    private String currency;

    private double interestRate;

    private double overdraft;

    private long clientId;
}
