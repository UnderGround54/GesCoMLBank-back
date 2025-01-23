package org.gescomlbank.controller;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.services.bankaccounts.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    BankAccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/accounts")
    void createBankAccount(@RequestBody BankAccountDto bankAccountDto) {
        this.bankAccountService.createBankAccount(bankAccountDto);
    }

    @GetMapping("/accounts/type/{type}")
    List<?> findAll(@PathVariable("type") String type) {
        if (type.equals("CC"))
            return this.bankAccountService.findCurrentAccounts();

        if (type.equals("CS"))
            return this.bankAccountService.findSavingAccounts();

        return null;
    }

    @GetMapping("/accounts/{numAccount}/{type}")
    ResponseEntity<?> findAccount(@PathVariable("numAccount") String numAccount, @PathVariable("type") String type) {
        BankAccount bankAccount = this.bankAccountService.findOne(numAccount);

        if (type.equals("CC") && (bankAccount instanceof CurrentAccount))
            return ResponseEntity.ok((CurrentAccount)bankAccount);
        if (type.equals("CS") && (bankAccount instanceof SavingAccount))
            return ResponseEntity.ok((SavingAccount)bankAccount);
        return null;
    }

    @GetMapping("/accounts/active/{numAccount}")
    boolean active(@PathVariable("numAccount") String numAccount) {
        return this.bankAccountService.activeAccount(numAccount);
    }

    @GetMapping("/accounts/suspend/{numAccount}")
    boolean suspend(@PathVariable("numAccount") String numAccount) {
        return this.bankAccountService.suspendAccount(numAccount);
    }
}
