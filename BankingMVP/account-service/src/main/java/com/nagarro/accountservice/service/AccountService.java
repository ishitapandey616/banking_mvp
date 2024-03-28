package com.nagarro.accountservice.service;

import com.nagarro.accountservice.model.Account;
import com.nagarro.accountservice.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account addMoney(Long accountId, BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            BigDecimal currentBalance = account.getBalance();
            BigDecimal newBalance = currentBalance.add(amount);
            account.setBalance(newBalance);
            return accountRepository.save(account);
        } else {
            // Handle account not found
            return null;
        }
    }
    
    public Account createAccount(Long customerId, BigDecimal initialBalance) {
        // Create a new account entity
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setBalance(initialBalance);

        // Save the account entity to the database
        return accountRepository.save(account);
    }

    public Account withdrawMoney(Long accountId, BigDecimal amount) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            BigDecimal currentBalance = account.getBalance();
            if (currentBalance.compareTo(amount) >= 0) {
                BigDecimal newBalance = currentBalance.subtract(amount);
                account.setBalance(newBalance);
                return accountRepository.save(account);
            } else {
                // Handle insufficient balance
                return null;
            }
        } else {
            // Handle account not found
            return null;
        }
    }

    public Optional<Account> getAccountDetails(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public boolean deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
		return true;
    }

    public boolean deleteAccountsByCustomerId(Long customerId) {
        accountRepository.deleteById(customerId);
        return true;
    }

    public boolean validateCustomer(Long customerId) {
        // Use WebClient to validate customer details from Customer Management Service
        WebClient webClient = webClientBuilder.baseUrl("http://192.168.1.122:8002").build();
        Mono<Boolean> result = webClient.get()
                .uri("/customers/{customerId}/validate", customerId)
                .retrieve()
                .bodyToMono(Boolean.class);
        return result.block();
    }
}
