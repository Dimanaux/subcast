package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Podcast;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping(path = {"/subscriptions"})
public class SubscriptionsController implements CommonResponses {
    private final AccountRepository accountRepository;
    private final PodcastRepository podcastRepository;

    @Autowired
    public SubscriptionsController(AccountRepository accountRepository,
                                   PodcastRepository podcastRepository) {
        this.accountRepository = accountRepository;
        this.podcastRepository = podcastRepository;
    }

    @ResponseBody
    @GetMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map getSubscriptions(@RequestParam String token) {
        Account account = accountRepository.findByToken(token);
        if (account != null) {
            return new TreeMap<String, Object>() {{
                putAll(STATUS_OK);
                put("subscriptions", podcastRepository.findAllByAccountId(account.getId()));
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
    public Map addSubscription(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String feedUrl = body.get("podcastFeedUrl");
        long podcastId = Long.parseLong(body.get("podcastId"));

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            podcastRepository.save(new Podcast(podcastId, feedUrl));
            podcastRepository.createSubscription(account.getId(), podcastId);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @DeleteMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map deleteSubscription(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        long podcastId = Long.parseLong(body.get("podcastId"));

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            podcastRepository.deleteSubscription(account.getId(), podcastId);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }
}
