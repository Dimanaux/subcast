package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Progress;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.ProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = {"/progress"})
public class ProgressController implements CommonResponses {
    private final AccountRepository accountRepository;
    private final ProgressRepository progressRepository;

    @Autowired
    public ProgressController(AccountRepository accountRepository, ProgressRepository progressRepository) {
        this.accountRepository = accountRepository;
        this.progressRepository = progressRepository;
    }

    @ResponseBody
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map getProgress(@RequestParam("token") String tokenAsString,
                           @RequestParam(value = "guid", required = false) String guid) {
        Token token = new Token(tokenAsString);
        Account account = accountRepository.findByToken(token);
        if (account != null) {
            if (guid != null && !guid.isEmpty()) {
                Progress p = progressRepository.findByAccountIdAndGuid(account.getId(), guid);
                return new TreeMap<String, Object>() {{
                    putAll(STATUS_OK);
                    put("progress", p);
                }};
            } else {
                return new TreeMap<String, Object>() {{
                    putAll(STATUS_OK);
                    put("progress", progressRepository.findAllByAccountId(account.getId()));
                }};
            }
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map updateProgress(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String guid = body.get("guid");
        int time = Integer.parseInt(body.get("time"));

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            progressRepository.saveProgress(account.getId(), guid, time);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map deleteProgress(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String guid = body.get("guid");

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            progressRepository.deleteProgress(account.getId(), guid);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }
}
