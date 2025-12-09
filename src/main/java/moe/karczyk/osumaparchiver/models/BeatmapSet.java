package moe.karczyk.osumaparchiver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany
    private List<Beatmap> beatmaps;

    private boolean selectedToArchive;

}
