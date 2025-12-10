package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BigPictureViewModel {
    private final BeatmapSetService beatmapSetService;
    public final SimpleObjectProperty<Image> beatmapSetImg = new SimpleObjectProperty<>(null);

    public void showBeatmapSet(long beatmapSetId) {
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId).orElseThrow();
        try {
            beatmapSetImg.setValue(new Image(new FileInputStream(Path.of(beatmapSet.getFullDirectoryPath(), beatmapSet.getBeatmaps().getFirst().getBackgroundFilename()).toFile())));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }


    }
}
