package org.gescomlbank.controller;

import org.gescomlbank.dtos.BankAccountDto;
import org.gescomlbank.services.bankaccounts.BankAccountService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/accounts/type")
    ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
                    ) {
        Pageable pageable = PageRequest.of(page, pageSize);

        if ("CC".equalsIgnoreCase(type))
            return this.bankAccountService.findCurrentAccounts(pageable);

        if ("CS".equalsIgnoreCase(type))
            return this.bankAccountService.findSavingAccounts(pageable);

        return this.bankAccountService.findAllAccounts(pageable);
    }

    @GetMapping("/accounts/{numAccount}/{type}")
    ResponseEntity<Map<String, Object>> findAccount(@PathVariable("numAccount") String numAccount, @PathVariable("type") String type) {
        return this.bankAccountService.findOne(numAccount, type);
    }

    @GetMapping("/accounts/active/{numAccount}")
    ResponseEntity<Map<String, Object>> active(@PathVariable("numAccount") String numAccount) {
        return this.bankAccountService.activeAccount(numAccount);
    }

    @GetMapping("/accounts/suspend/{numAccount}")
    ResponseEntity<Map<String, Object>> suspend(@PathVariable("numAccount") String numAccount) {
        return this.bankAccountService.suspendAccount(numAccount);
    }
}
