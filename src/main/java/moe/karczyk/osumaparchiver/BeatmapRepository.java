package moe.karczyk.osumaparchiver;

import moe.karczyk.osumaparchiver.models.Beatmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeatmapRepository extends JpaRepository<Beatmap, Long> {
}
