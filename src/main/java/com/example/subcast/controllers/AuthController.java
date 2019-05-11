package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;
import com.example.subcast.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = {"/auth"})
public class AuthController implements CommonResponses {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @ResponseBody
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map authenticate(@RequestBody Account account) {
        Token token = authService.authenticate(account);
        if (token != null) {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_OK);
                put("token", token.toString());
            }};
        } else {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_ERROR);
                put("errorMessage", "Wrong username or password.");
            }};
        }
    }
}
