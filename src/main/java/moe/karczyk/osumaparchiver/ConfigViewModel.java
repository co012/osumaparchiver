package moe.karczyk.osumaparchiver;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.eventpassing.Event;
import moe.karczyk.osumaparchiver.eventpassing.Producer;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.UrlEncodingService;
import moe.karczyk.osumaparchiver.ui.models.BeatmapPresent;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ConfigViewModel {
    private final static String DEFAULT_IMG_PNG = "src/resources/default_img.png";

    private final BeatmapSetService beatmapSetService;
    private final BigPictureViewModel bigPictureViewModel;
    private final UrlEncodingService urlEncodingService;
    private final Producer producer;

    public final ObservableList<BeatmapSetPresent> visibleBeatmapSets = FXCollections.observableArrayList();
    public final ObservableList<BeatmapPresent> availableBeatmaps = FXCollections.observableArrayList();
    public final ObjectProperty<BeatmapPresent> activeBeatmap = new SimpleObjectProperty<>(BeatmapPresent.EMPTY);


    public int getPageCount(int pageSize) {
        return Math.toIntExact(Math.ceilDiv(beatmapSetService.getBeatmapSetsCount(), pageSize));

    }

    public void changePage(int pageIdx, int pageSize) {
        visibleBeatmapSets.clear();
        beatmapSetService.getBeatmapSetsPage(pageIdx, pageSize)
                .stream()
                .map(map -> BeatmapSetPresent.builder()
                        .id(map.getId())
                        .name(map.getName())
                        .artists(String.join(", ", map.getArtists()))
                        .creators(String.join(", ", map.getCreators()))
                        .beatmapCount(map.getBeatmaps().size())
                        .archive(map.isSelectedToArchive())
                        .build())
                .forEach(visibleBeatmapSets::add);

    }

    public void changeBeatmapSetsArchiveStatus(List<BeatmapSetPresent> beatmapSets, boolean archive) {
        var selectedIds = beatmapSets.stream()
                .map(BeatmapSetPresent::id)
                .toList();
        beatmapSetService.changeArchiveSelectionStatus(selectedIds, archive);
    }


    public void openBigPictureOn(BeatmapSetPresent target) {
        bigPictureViewModel.changeBeatmapSet(target.id());
        producer.publish(Event.BIG_PICTURE_TARGET_SELECTED);
    }

    public void loadAvailableBeatmapsInSet(long beatmapSetId) {
        availableBeatmaps.clear();
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId)
                .orElseThrow();
        beatmapSet.getBeatmaps()
                .stream()
                .map(map -> BeatmapPresent.builder()
                        .title(Objects.requireNonNullElse(map.getTitle(), "N/A"))
                        .titleOriginal(Objects.requireNonNullElse(map.getTitleOriginal(), "N/A"))
                        .artist(Objects.requireNonNullElse(map.getArtist(), "N/A"))
                        .artistOriginal(Objects.requireNonNullElse(map.getArtistOriginal(), "N/A"))
                        .creator(Objects.requireNonNullElse(map.getCreator(), "N/A"))
                        .version(Objects.requireNonNullElse(map.getVersion(), "Id: " + map.getId()))
                        .beatmapImgUrl(
                                map.getBackgroundFilename() == null ?
                                        urlEncodingService.encodePath(Path.of(DEFAULT_IMG_PNG))
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




}
