package moe.karczyk.osumaparchiver.repositories;

import moe.karczyk.osumaparchiver.models.BeatmapSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeatmapSetRepository extends JpaRepository<BeatmapSet, Long> {

}
