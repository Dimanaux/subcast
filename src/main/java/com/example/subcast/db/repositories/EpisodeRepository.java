package com.example.subcast.db.repositories;

import com.example.subcast.db.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, String> {
    @Modifying
    @Transactional
    default Episode saveOrUpdate(Episode episode) {
        Optional<Episode> byId = findById(episode.getGuid());

        if (!byId.isPresent()) {
            return save(episode);
        }

        Episode dbEpisode = byId.get();

        if (dbEpisode.getLink() == null) {
            dbEpisode.setLink(episode.getLink());
        }

        if (dbEpisode.getName() == null) {
            dbEpisode.setLink(episode.getLink());
        }

        save(dbEpisode);

        return dbEpisode;
    }
}
