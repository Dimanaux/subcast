package com.example.subcast.controllers;

import com.example.subcast.db.Account;
import com.example.subcast.db.Episode;
import com.example.subcast.db.Podcast;
import com.example.subcast.db.Token;
import com.example.subcast.db.repositories.AccountRepository;
import com.example.subcast.db.repositories.EpisodeRepository;
import com.example.subcast.db.repositories.PlayLaterRepository;
import com.example.subcast.db.repositories.PodcastRepository;
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
    private final PodcastRepository podcastRepository;
    private final EpisodeRepository episodeRepository;

    @Autowired
    public PlayListController(AccountRepository accountRepository, PlayLaterRepository playListController, PodcastRepository podcastRepository, EpisodeRepository episodeRepository) {
        this.accountRepository = accountRepository;
        this.playListController = playListController;
        this.podcastRepository = podcastRepository;
        this.episodeRepository = episodeRepository;
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
        Long podcastId = Long.parseLong(body.get("podcastId"));
        String podcastFeedUrl = body.get("podcastFeedUrl");
        String link = body.get("link");

        podcastRepository.save(new Podcast(podcastId, podcastFeedUrl));

        Episode episode = new Episode();
        episode.setGuid(guid);
        episode.setPodcastId(podcastId);
        episode.setLink(link);
        episodeRepository.save(episode);

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
