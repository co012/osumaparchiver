package moe.karczyk.osumaparchiver;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.models.BeatmapSet;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.UrlEncodingService;
import moe.karczyk.osumaparchiver.ui.models.BeatmapPresent;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BeatmapSetViewModel {

    private final BeatmapSetService beatmapSetService;
    private final UrlEncodingService urlEncodingService;

    public final ObservableList<BeatmapPresent> availableBeatmaps = FXCollections.observableArrayList();
    public final ObjectProperty<BeatmapPresent> activeBeatmap = new SimpleObjectProperty<>(BeatmapPresent.EMPTY);
    public final ObjectProperty<BeatmapSetPresent> beatmapSet = new SimpleObjectProperty<>(BeatmapSetPresent.EMPTY);

    public void changeBeatmapSet(long beatmapSetId) {
        availableBeatmaps.clear();
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId)
                .orElseThrow();

        changeBeatmaps(beatmapSet);
        activeBeatmap.set(availableBeatmaps.getFirst());
        var artists = String.join(", ", beatmapSet.getArtists());
        var creators = String.join(", ", beatmapSet.getCreators());

        this.beatmapSet.set(BeatmapSetPresent.builder()
                .id(beatmapSetId)
                .beatmapCount(beatmapSet.getBeatmaps().size())
                .name(beatmapSet.getName())
                .artists(artists)
                .creators(creators)
                .archive(beatmapSet.isSelectedToArchive())
                .build()
        );


    }

    private void changeBeatmaps(final BeatmapSet beatmapSet) {
        beatmapSet.getBeatmaps()
                .stream()
                .map(map -> BeatmapPresent.builder()
                        .title(Objects.requireNonNullElse(map.getTitle(), BeatmapPresent.EMPTY.artist()))
                        .titleOriginal(Objects.requireNonNullElse(map.getTitleOriginal(), BeatmapPresent.EMPTY.titleOriginal()))
                        .artist(Objects.requireNonNullElse(map.getArtist(), BeatmapPresent.EMPTY.artist()))
                        .artistOriginal(Objects.requireNonNullElse(map.getArtistOriginal(), BeatmapPresent.EMPTY.artistOriginal()))
                        .creator(Objects.requireNonNullElse(map.getCreator(), BeatmapPresent.EMPTY.creator()))
                        .version(Objects.requireNonNullElse(map.getVersion(), "Id: " + map.getId()))
                        .beatmapImgUrl(
                                map.getBackgroundFilename() == null ?
                                        BeatmapPresent.EMPTY.beatmapImgUrl()
                                        :
                                        urlEncodingService.encodePath(Path.of(beatmapSet.getFullDirectoryPath(), map.getBackgroundFilename()))
                        )
                        .build())
                .forEach(availableBeatmaps::addLast);
    }

    public void setActiveBeatmap(int availableBeatmapIdx) {
        if (availableBeatmapIdx < 0 || availableBeatmapIdx > availableBeatmaps.size()) {
            activeBeatmap.set(BeatmapPresent.EMPTY);
            return;
        }
        activeBeatmap.set(availableBeatmaps.get(availableBeatmapIdx));
    }

    public void toggleArchiveSelectionForBeatmapSet() {
        var beatmapSetVal = beatmapSet.get();
        beatmapSetService.toggleArchiveSelectionForBeatmapSet(beatmapSetVal.id());
        changeBeatmapSet(beatmapSetVal.id());
    }

    //TODO: boundary check
    public void nextBeatmapSet() {
        changeBeatmapSet(beatmapSet.get().id() + 1);
    }

    public void previousBeatmapSet() {
        changeBeatmapSet(beatmapSet.get().id() - 1);
    }
}
