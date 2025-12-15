package moe.karczyk.osumaparchiver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BeatmapSet {

    @Id
    @GeneratedValue
    private Long id;

    private String directoryName;
    private String fullDirectoryPath;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Beatmap> beatmaps;

    private boolean selectedToArchive;

    private <R> List<R> getUniqueBeatmapFieldValues(Function<Beatmap, R> extractor) {
        return beatmaps.stream()
                .map(extractor)
                .distinct()
                .toList();
    }

    public List<String> getArtists() {
        return getUniqueBeatmapFieldValues(Beatmap::getArtist);
    }

    public List<String> getCreators() {
        return getUniqueBeatmapFieldValues(Beatmap::getCreator);
    }

}
