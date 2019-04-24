package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Podcast;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.PodcastRepository;
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
    @RequestMapping(
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map getSubscriptions(@RequestBody Token token) {
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
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map addSubscription(@RequestBody Map<String, String> body) {
        Token token = new Token(body.get("token"));
        String feedUrl = body.get("podcastFeedUrl");
        long podcastId = Long.parseLong(body.get("podcastId"));

        Account account = accountRepository.findByToken(token);
        if (account != null) {
            if (feedUrl != null && !feedUrl.isEmpty()) {
                podcastRepository.save(new Podcast(podcastId, feedUrl));
            }
            podcastRepository.createSubscription(account.getId(), podcastId);
            return STATUS_OK;
        } else {
            return INVALID_TOKEN;
        }
    }

    @ResponseBody
    @RequestMapping(
            method = RequestMethod.DELETE,
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
