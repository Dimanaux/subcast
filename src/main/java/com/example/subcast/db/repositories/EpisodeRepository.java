package com.example.subcast.db.repositories;

import com.example.subcast.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, String> {
    @Override
    @Modifying
    @Transactional
    default <S extends Episode> S save(S episode) {
        Optional<Episode> byId = findById(episode.getGuid());

        if (byId.map(Episode::getPodcastId).isPresent() && byId.map(Episode::getLink).isPresent()) {
            return (S) byId.get();
        }

        saveEpisode(episode.getGuid(), episode.getPodcastId(), episode.getLink());
        return (S) episode;
    }


    @Query(
            value = "INSERT INTO episode AS e (guid, podcast_id, link) " +
                    " VALUES (:guid, :podcastId, :link) " +
                    " ON CONFLICT (guid) DO UPDATE " +
                    " SET podcast_id = coalesce(e.podcast_id, :podcastId)," +
                    " link = coalesce(e.link, :link) ;",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void saveEpisode(@Param("guid") String episodeGuid, @Param("podcastId") long podcastId, @Param("link") String link);
}
