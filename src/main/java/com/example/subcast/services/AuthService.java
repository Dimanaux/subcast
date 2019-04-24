package com.example.subcast.services;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;

public interface AuthService {
    Token authenticate(Account account);

    boolean createAccount(Account account);

    boolean usernameTaken(String username);
}
