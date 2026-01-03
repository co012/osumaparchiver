package moe.karczyk.osumaparchiver;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapService;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.ui.UiCoordinator;
import moe.karczyk.osumaparchiver.ui.UiCoordinatorAware;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ConfigViewModel implements UiCoordinatorAware {

    private UiCoordinator uiCoordinator;

    private final BeatmapSetService beatmapSetService;
    private final BeatmapService beatmapService;

    public final ObservableList<BeatmapSetPresent> visibleBeatmapSets = FXCollections.observableArrayList();
    public final LongProperty beatmapSetCount = new SimpleLongProperty(0);
    public final LongProperty toArchiveBeatmapSetCount = new SimpleLongProperty(0);
    public final LongProperty beatmapCount = new SimpleLongProperty(0);


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
        refreshCounts();
    }

    public void refreshCounts() {
        beatmapCount.set(beatmapService.getBeatmapCount());
        toArchiveBeatmapSetCount.set(beatmapSetService.getToArchiveBeatmapSetCount());
        beatmapSetCount.set(beatmapSetService.getBeatmapSetCount());
    }


    public void openBigPictureOn() {
        uiCoordinator.openBigPicture();
    }

    public void requestArchiveAction() {
        uiCoordinator.requestArchiveAction();
    }

    @Override
    public void setUiCoordinator(UiCoordinator uiCoordinator) {
        this.uiCoordinator = uiCoordinator;
    }
}
