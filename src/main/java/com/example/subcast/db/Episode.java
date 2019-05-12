package com.example.subcast.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "episode")
public class Episode {
    @Id
    @Column(name = "guid")
    private String guid;

    @Column(name = "podcast_id")
    private Long podcastId;

    @Column(name = "link")
    private String link = "";

    public Episode() {
    }

    public String getGuid() {
        return guid;
    }

    public Episode setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public Long getPodcastId() {
        return podcastId;
    }

    public Episode setPodcastId(Long podcastId) {
        this.podcastId = podcastId;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Episode setLink(String link) {
        this.link = link;
        return this;
    }
}
