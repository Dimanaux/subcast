package com.example.subcast.db;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "history",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "episode_guid")}
    )
    private List<Episode> history = new LinkedList<>();

    @OneToMany
    @JoinColumn(name = "account_id")
    private List<Progress> progress = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "podcast_id")}
    )
    private List<Podcast> subscriptions = new LinkedList<>();

    @ManyToMany
    @JoinTable(
            name = "play_later",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "episode_guid")}
    )
    private List<Episode> playLater = new LinkedList<>();

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public Account setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Account setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Account setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<Episode> getHistory() {
        return history;
    }

    public Account setHistory(List<Episode> history) {
        this.history = history;
        return this;
    }

    public List<Progress> getProgress() {
        return progress;
    }

    public Account setProgress(List<Progress> progress) {
        this.progress = progress;
        return this;
    }

    public List<Podcast> getSubscriptions() {
        return subscriptions;
    }

    public Account setSubscriptions(List<Podcast> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public List<Episode> getPlayLater() {
        return playLater;
    }

    public Account setPlayLater(List<Episode> playLater) {
        this.playLater = playLater;
        return this;
    }
}
