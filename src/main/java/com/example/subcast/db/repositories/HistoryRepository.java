package com.example.subcast.db.repositories;

import com.example.subcast.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<Episode, String> {
    @Override
    @Modifying
    @Transactional
    default <E extends Episode> E save(E episode) {
        Optional<Episode> episodeFromDb = findById(episode.getGuid());

        if (episodeFromDb.isPresent()) {
            return (E) episodeFromDb.get();
        }

        if (episode.getLink() != null && !episode.getLink().isEmpty()) {
            saveEpisode(episode.getGuid(), episode.getPodcastId(), episode.getLink());
        }
        return episode;
    }

    @Query(
            value = "INSERT INTO episode (guid, podcast_id, link) " +
                    " VALUES (:guid, :podcastId, :link)",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void saveEpisode(@Param("guid") String guid,
                     @Param("podcastId") Long podcastId,
                     @Param("link") String link);

    @Query(
            value = "INSERT INTO history (episode_guid, account_id) " +
                    " VALUES (:guid, :accountId)",
            nativeQuery = true
    )
    @Modifying
    @Transactional
    void saveToHistory(@Param("accountId") Long accountId,
                       @Param("guid") String guid);

    @Query(
            value = "SELECT h.episode_guid AS guid, " +
                    " coalesce(e.podcast_id) AS podcast_id, " +
                    " coalesce(e.link) AS link " +
                    " FROM history h " +
                    " LEFT JOIN episode e ON h.episode_guid = e.guid " +
                    " WHERE account_id = :accountId ",
            nativeQuery = true
    )
    List<Episode> findAllByAccountId(@Param("accountId") Long accountId);
}
