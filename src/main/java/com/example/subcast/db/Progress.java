package com.example.subcast.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * represents someone's progress on particular podcast episode
 */
@Entity
@Table(name = "progress")
@IdClass(ProgressCompositeKey.class)
public class Progress {
    // whose progress (account identifier)
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Id
    @Column(name = "episode_guid")
    private String episodeGuid;

    // time listened in seconds
    @Column(name = "time")
    private Integer time;

    public Progress() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public Progress setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getEpisodeGuid() {
        return episodeGuid;
    }

    public Progress setEpisodeGuid(String episodeGuid) {
        this.episodeGuid = episodeGuid;
        return this;
    }

    public Integer getTime() {
        return time;
    }

    public Progress setTime(Integer time) {
        this.time = time;
        return this;
    }
}

@Embeddable
class ProgressCompositeKey implements Serializable {
    private Long accountId;
    private String episodeGuid;

    public ProgressCompositeKey() {
    }

    public ProgressCompositeKey(Long accountId, String episodeGuid) {
        this.accountId = accountId;
        this.episodeGuid = episodeGuid;
    }

    public Long getAccountId() {
        return accountId;
    }

    public ProgressCompositeKey setAccountId(Long accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getEpisodeGuid() {
        return episodeGuid;
    }

    public ProgressCompositeKey setEpisodeGuid(String episodeGuid) {
        this.episodeGuid = episodeGuid;
        return this;
    }
}
