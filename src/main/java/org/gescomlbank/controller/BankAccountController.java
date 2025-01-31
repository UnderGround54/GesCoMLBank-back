package org.gescomlbank.controller;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.gescomlbank.services.bankaccounts.BankAccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    BankAccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/accounts")
    ResponseEntity<Map<String, Object>> createBankAccount(@RequestBody BankAccountDto bankAccountDto) {
        return this.bankAccountService.createBankAccount(bankAccountDto);
    }

    @GetMapping("/accounts/type/{type}")
    ResponseEntity<Map<String, Object>> findAll(
            @PathVariable("type") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
                    ) {
        Pageable pageable = PageRequest.of(page, pageSize);
        if (type.equals("CC"))
            return this.bankAccountService.findCurrentAccounts(pageable);

        if (type.equals("CS"))
            return this.bankAccountService.findSavingAccounts(pageable);

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
