package com.example.subcast.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "podcast")
public class Podcast {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "feed_url", columnDefinition = "TEXT NULL DEFAULT NULL")
    private String feedUrl;

    public Podcast() {
    }

    public Podcast(Long id, String feedUrl) {
        this.id = id;
        this.feedUrl = feedUrl;
    }

    public Podcast(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getTrackId() {
        return id;
    }

    public Podcast setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public Podcast setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
        return this;
    }
}
