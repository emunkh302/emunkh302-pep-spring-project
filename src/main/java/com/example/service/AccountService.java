package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Create a new account with validation
    public Account createAccount(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            return null;
        }
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return null; // Username already exists
        }
        return accountRepository.save(account);
    }

    // Retrieve an account by ID
    public Account getAccountById(Integer id) {
        return accountRepository.findById(id).orElse(null);
    }

    // Retrieve an account by username
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    // User login validation
    public Account login(Account account) {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }
}
