package com.example.subcast.controllers;

import com.example.app.subcast.db.Account;
import com.example.app.subcast.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = {"/register"})
public class RegisterController implements CommonResponses {
    private AuthService authService;

    @Autowired
    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseBody
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map register(@RequestBody Account account) {
        if (authService.createAccount(account)) {
            return STATUS_OK;
        } else {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_ERROR);
                put("errorMessage", "Username is already taken.");
            }};
        }
    }
}
