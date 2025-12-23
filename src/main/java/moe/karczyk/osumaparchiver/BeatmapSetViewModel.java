package moe.karczyk.osumaparchiver;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.models.BeatmapSet;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.UrlEncodingService;
import moe.karczyk.osumaparchiver.ui.models.BeatmapPresent;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BeatmapSetViewModel {

    private final BeatmapSetService beatmapSetService;
    private final UrlEncodingService urlEncodingService;

    public final ObjectProperty<List<BeatmapPresent>> availableBeatmaps = new SimpleObjectProperty<>(List.of());
    public final ObjectProperty<BeatmapPresent> activeBeatmap = new SimpleObjectProperty<>(BeatmapPresent.EMPTY);
    public final ObjectProperty<BeatmapSetPresent> beatmapSet = new SimpleObjectProperty<>(BeatmapSetPresent.EMPTY);

    public void changeBeatmapSet(long beatmapSetId) {
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId)
                .orElseThrow();

        changeBeatmaps(beatmapSet);
        activeBeatmap.set(availableBeatmaps.get().stream().findFirst().orElse(BeatmapPresent.EMPTY));
        var artists = String.join(", ", beatmapSet.getArtists());
        var creators = String.join(", ", beatmapSet.getCreators());
        var nextId = beatmapSetService.findNext(beatmapSetId).map(BeatmapSet::getId).orElse(null);
        var previousId = beatmapSetService.findPrevious(beatmapSetId).map(BeatmapSet::getId).orElse(null);

        this.beatmapSet.set(BeatmapSetPresent.builder()
                .id(beatmapSetId)
                .beatmapCount(beatmapSet.getBeatmaps().size())
                .name(beatmapSet.getName())
                .artists(artists)
                .creators(creators)
                .archive(beatmapSet.isSelectedToArchive())
                .nextId(nextId)
                .previousId(previousId)
                .build()
        );


    }

    private void changeBeatmaps(final BeatmapSet beatmapSet) {
        var newBeatmaps = beatmapSet.getBeatmaps()
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
                .toList();
        availableBeatmaps.set(newBeatmaps);
    }

    public void setActiveBeatmap(int availableBeatmapIdx) {
        if (availableBeatmapIdx < 0 || availableBeatmapIdx > availableBeatmaps.get().size()) {
            activeBeatmap.set(BeatmapPresent.EMPTY);
            return;
        }
        activeBeatmap.set(availableBeatmaps.get().get(availableBeatmapIdx));
    }

    public void toggleArchiveSelectionForBeatmapSet() {
        var beatmapSetVal = beatmapSet.get();
        beatmapSetService.toggleArchiveSelectionForBeatmapSet(beatmapSetVal.id());
        changeBeatmapSet(beatmapSetVal.id());
    }

    public boolean nextBeatmapSet() {
        var beatmapSet = this.beatmapSet.get();
        if (beatmapSet.hasNext()) {
            changeBeatmapSet(beatmapSet.nextId());
            return true;
        }
        return false;
    }

    public boolean previousBeatmapSet() {
        var beatmapSet = this.beatmapSet.get();
        if (beatmapSet.hasPrevious()) {
            changeBeatmapSet(beatmapSet.previousId());
            return true;
        }
        return false;
    }
}
