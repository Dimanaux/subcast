package com.example.subcast.services;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    private AccountRepository accountRepository;

    @Autowired
    public AuthServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Token authenticate(Account account) {
        String username = account.getUsername();
        String password = encrypt(account.getPassword());

        Account accountByUsername = accountRepository.findByUsername(username);

        // wrong username
        if (accountByUsername == null) {
            return null;
        }

        // correct username, wrong password
        if (!accountByUsername.getPassword().equals(password)) {
            return null;
        }

        Token token = createToken(accountByUsername);
        accountRepository.saveToken(accountByUsername.getId(), token.toString());
        return token;
    }

    @Override
    public boolean createAccount(Account account) {
        if (!usernameTaken(account.getUsername())) {
            accountRepository.save(account);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean usernameTaken(String username) {
        return accountRepository.findByUsername(username) != null;
    }

    private Token createToken(Account account) {
        String token = encrypt(account.getUsername() + LocalDateTime.now());
        return new Token(token);
    }

    private String encrypt(String password) {
        // todo add real encryption
        return password;
    }
}
