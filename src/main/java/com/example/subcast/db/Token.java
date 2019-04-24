package com.example.subcast.db;

import javax.persistence.*;

@Entity
@Table(name = "account_token")
public class Token {
    @Id
    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public Token setAccount(Account account) {
        this.account = account;
        return this;
    }

    @Override
    public String toString() {
        return token;
    }
}
