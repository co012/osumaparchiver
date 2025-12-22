package moe.karczyk.osumaparchiver.repositories;

import moe.karczyk.osumaparchiver.models.BeatmapSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeatmapSetRepository extends JpaRepository<BeatmapSet, Long> {

    @Modifying
    @Query("update BeatmapSet map set map.selectedToArchive = :archive where map.id in :ids")
    void updateArchiveSelectionStatus(List<Long> ids, boolean archive);

    Optional<BeatmapSet> findFirstByIdAfterOrderByIdAsc(long idAfter);

    Optional<BeatmapSet> findFirstByIdBeforeOrderByIdDesc(long idAfter);

}
