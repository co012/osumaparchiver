package moe.karczyk.osumaparchiver.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Beatmap {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String titleOriginal;
    private String songArtist;
    private String songArtistOriginal;
    private String mapCreator;
    private String mapVersion;

    private String songFileName;
    private String backgroundFileName;

}
