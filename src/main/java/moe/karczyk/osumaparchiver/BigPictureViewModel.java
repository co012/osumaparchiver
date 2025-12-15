package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.UrlEncodingService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BigPictureViewModel {
    private final BeatmapSetService beatmapSetService;
    private final UrlEncodingService urlEncodingService;

    public final SimpleStringProperty backgroundUri = new SimpleStringProperty();
    public final SimpleStringProperty title = new SimpleStringProperty();
    public final SimpleStringProperty artists = new SimpleStringProperty();
    public final SimpleStringProperty creators = new SimpleStringProperty();
    public final SimpleLongProperty currentBeatmapSetId = new SimpleLongProperty();


    public void changeBeatmapSet(long beatmapSetId) {
        currentBeatmapSetId.set(beatmapSetId);
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId).orElseThrow();
        var backgroundPath = Path.of(beatmapSet.getFullDirectoryPath(), beatmapSet.getBeatmaps().getFirst().getBackgroundFilename());
        backgroundUri.set(urlEncodingService.encodePath(backgroundPath));

        title.set(beatmapSet.getName());

        var artists = String.join(", ", beatmapSet.getArtists());
        this.artists.set(artists);

        var creators = String.join(", ", beatmapSet.getCreators());
        this.creators.set(creators);
    }

    //TODO: boundary check
    public void nextBeatmapSet() {
        changeBeatmapSet(currentBeatmapSetId.get() + 1);
    }

    public void previousBeatmapSet() {
        changeBeatmapSet(currentBeatmapSetId.get() - 1);
    }
}
