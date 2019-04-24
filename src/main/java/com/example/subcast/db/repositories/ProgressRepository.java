package com.example.subcast.db.repositories;

import com.example.app.subcast.db.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * repository to manage progress in podcasts episodes
 */
@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    /**
     * update OR CREATE progress
     *
     * @param accountId   whose progress
     * @param episodeGuid episode identifier
     * @param time        time listened in seconds
     */
    @Query(
            value = "INSERT INTO progress (account_id, episode_guid, time) " +
                    " VALUES (:account, :guid, :time) " +
                    " ON CONFLICT (account_id, episode_guid) DO UPDATE SET time = :time",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void saveProgress(@Param("account") long accountId,
                      @Param("guid") String episodeGuid,
                      @Param("time") int time);

    /**
     * get all progress by account
     *
     * @param accountId whose progress
     * @return all the progress objects of the account
     */
    @Query(
            value = "SELECT * FROM progress WHERE account_id = :accountId ",
            nativeQuery = true
    )
    List<Progress> findAllByAccountId(@Param("accountId") long accountId);

    /**
     * get progress for particular episode
     *
     * @param accountId whose progress
     * @param guid      episode identifier
     * @return progress object
     */
    @Query(
            value = "SELECT * FROM progress WHERE " +
                    " account_id = :account AND episode_guid = :guid",
            nativeQuery = true
    )
    Progress findByAccountIdAndGuid(@Param("account") long accountId,
                                    @Param("guid") String guid);

    /**
     * delete progress for particular episode
     *
     * @param accountId whose progress
     * @param guid      episode identifier
     */
    @Query(
            value = "DELETE FROM progress WHERE " +
                    " account_id = :account AND episode_guid = :guid",
            nativeQuery = true
    )
    @Transactional
    @Modifying
    void deleteProgress(@Param("account") long accountId,
                        @Param("guid") String guid);
}
