package com.nagarro.accountservice.controller;


import com.nagarro.accountservice.model.Account;
import com.nagarro.accountservice.service.AccountService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    

    @PostMapping("/{accountId}/add-money")
    public ResponseEntity<?> addMoneyToAccount(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        Account account = accountService.addMoney(accountId, amount);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/withdraw-money")
    public ResponseEntity<?> withdrawMoneyFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        Account account = accountService.withdrawMoney(accountId, amount);
        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found or insufficient balance");
        }
        return ResponseEntity.ok(account);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountDetails(@PathVariable Long accountId) {
        Optional<Account> account = accountService.getAccountDetails(accountId);
        if(account.isPresent()) {
            return ResponseEntity.ok(account.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }
    
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account request) {
        Account account = accountService.createAccount(request.getCustomerId(), request.getBalance());
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        boolean result = accountService.deleteAccount(accountId);
        if(result) {
            return ResponseEntity.ok("Account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }
    }
    
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<?> deleteAccountsByCustomerId(@PathVariable Long customerId) {
        boolean result = accountService.deleteAccountsByCustomerId(customerId);
        if(result) {
            return ResponseEntity.ok("Accounts of customer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }
}