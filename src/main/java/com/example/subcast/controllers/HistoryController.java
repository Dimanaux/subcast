package com.example.subcast.controllers;

import com.example.app.subcast.db.Account;
import com.example.app.subcast.db.Token;
import com.example.app.subcast.db.repositories.AccountRepository;
import com.example.app.subcast.db.repositories.HistoryRepository;
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
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map getHistory(@RequestBody Token token) {
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
    @RequestMapping(
            method = RequestMethod.POST,
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
