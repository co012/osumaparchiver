package moe.karczyk.osumaparchiver.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Beatmap {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String titleOriginal;
    private String artist;
    private String artistOriginal;
    private String creator;
    private String version;

    private String audioFilename;
    private Long previewTime;
    private String backgroundFilename;

}
