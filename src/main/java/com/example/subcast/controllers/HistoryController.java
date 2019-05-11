package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/history")
public class HistoryController implements CommonResponses {
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryController(AccountRepository accountRepository, HistoryRepository historyRepository) {
        this.accountRepository = accountRepository;
        this.historyRepository = historyRepository;
    }

    @ResponseBody
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map getHistory(@RequestParam String token) {
        Account account = accountRepository.findByToken(token);
        if (account != null) {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_OK);
                put("history", historyRepository.findAllByAccountId(account.getId()));
            }};
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map addToHistory(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String podcastId = body.get("podcastId");
        String guid = body.get("episodeGuid");
        String episodeLink = body.get("episodeLink");

        Account account = accountRepository.findByToken(token);

        if (account != null) {
            if (podcastId != null && !podcastId.isEmpty() && episodeLink != null && !episodeLink.isEmpty()) {
                historyRepository.saveEpisode(guid, Long.parseLong(podcastId), episodeLink);
            }
            historyRepository.saveToHistory(account.getId(), guid);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }
}
