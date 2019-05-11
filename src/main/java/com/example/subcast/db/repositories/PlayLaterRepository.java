package com.example.subcast.db.repositories;

import com.example.subcast.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PlayLaterRepository extends JpaRepository<Episode, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO play_later (account_id, episode_guid) VALUES (:accountId, :episodeGuid );", nativeQuery = true)
    void addToPlayLater(@Param("accountId") long accountId,
                        @Param("episodeGuid") String episodeGuid);

    @Query(
            value = "SELECT p.episode_guid AS guid, " +
                    " coalesce(e.podcast_id) AS podcast_id, " +
                    " coalesce(e.link) AS link " +
                    " FROM play_later p " +
                    " LEFT JOIN episode e ON p.episode_guid = e.guid " +
                    " WHERE account_id = :accountId ",
            nativeQuery = true
    )
    List<Episode> findAllByAccountId(@Param("accountId") Long accountId);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM play_later WHERE account_id = :accountId AND episode_guid = :episodeGuid ;",
            nativeQuery = true
    )
    void removeFromPlayLater(@Param("accountId") Long accountId,
                             @Param("episodeGuid") String episodeGuid);
}
