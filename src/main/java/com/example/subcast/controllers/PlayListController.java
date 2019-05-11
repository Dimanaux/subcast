package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.PlayLaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = "/play_later")
public class PlayListController implements CommonResponses {
    private final AccountRepository accountRepository;
    private final PlayLaterRepository playListController;

    @Autowired
    public PlayListController(AccountRepository accountRepository, PlayLaterRepository playListController) {
        this.accountRepository = accountRepository;
        this.playListController = playListController;
    }

    @ResponseBody
    @GetMapping
    public Map<String, ?> getList(@RequestParam String token) {
        Account account = accountRepository.findByToken(token);
        if (account != null) {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_OK);
                put("playLater", playListController.findAllByAccountId(account.getId()));
            }};
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ?> addToList(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String guid = body.get("guid");

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            playListController.addToPlayLater(account.getId(), guid);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ?> remove(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String guid = body.get("guid");

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            playListController.removeFromPlayLater(account.getId(), guid);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }
}
